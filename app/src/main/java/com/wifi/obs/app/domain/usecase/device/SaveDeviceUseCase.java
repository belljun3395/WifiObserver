package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.converter.MemberConverter;
import com.wifi.obs.app.domain.converter.WifiServiceConverter;
import com.wifi.obs.app.domain.model.member.Member;
import com.wifi.obs.app.domain.model.wifi.WifiService;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.service.wifi.ValidatedWifiServiceService;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.app.exception.domain.AlreadyRegisterException;
import com.wifi.obs.app.exception.domain.OverLimitException;
import com.wifi.obs.app.web.dto.request.device.SaveDeviceRequest;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.device.DeviceType;
import com.wifi.obs.data.mysql.entity.support.WifiServiceEntitySupporter;
import com.wifi.obs.data.mysql.repository.device.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveDeviceUseCase {

	private final DeviceRepository deviceRepository;

	private final ValidatedWifiServiceService validatedWifiServiceService;
	private final ValidatedMemberService validatedMemberService;

	private final MemberConverter memberConverter;
	private final WifiServiceConverter wifiServiceConverter;

	private final IdMatchValidator idMatchValidator;

	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId, SaveDeviceRequest request) {

		WifiService service = getService(request.getSid());

		Member member = getMember(memberId);

		idMatchValidator.validate(member.getId(), service.getMemberId());
		checkConstraint(member, service.getId());
		isExist(service.getId(), request.getMac().toUpperCase());

		deviceRepository.save(
				DeviceEntity.builder()
						.type(DeviceType.valueOf(request.getDeviceType().name()))
						.mac(request.getMac().toUpperCase())
						.wifiService(wifiServiceEntitySupporter.getReferenceEntity(service.getId()))
						.build());
	}

	private Member getMember(Long memberId) {
		return memberConverter.from(validatedMemberService.execute(memberId));
	}

	private WifiService getService(Long serviceId) {
		return wifiServiceConverter.from(validatedWifiServiceService.execute(serviceId));
	}

	private void checkConstraint(Member member, Long serviceId) {
		if (member.isOverDeviceMax(getSavedDeviceCount(serviceId))) {
			throw new OverLimitException();
		}
	}

	private long getSavedDeviceCount(Long serviceId) {
		return deviceRepository
				.findAllByWifiServiceAndDeletedFalse(
						wifiServiceEntitySupporter.getReferenceEntity(serviceId))
				.size();
	}

	private void isExist(Long serviceId, String mac) {
		if (isExistDevice(serviceId, mac)) {
			throw new AlreadyRegisterException();
		}
	}

	private boolean isExistDevice(Long serviceId, String mac) {
		return deviceRepository.existsByMacAndWifiServiceAndDeletedFalse(
				mac, wifiServiceEntitySupporter.getReferenceEntity(serviceId));
	}
}
