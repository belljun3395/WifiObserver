package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.converter.DeviceConverter;
import com.wifi.obs.app.domain.converter.MemberConverter;
import com.wifi.obs.app.domain.converter.WifiServiceDeviceConverter;
import com.wifi.obs.app.domain.model.device.WifiServiceDevice;
import com.wifi.obs.app.domain.model.member.Member;
import com.wifi.obs.app.domain.model.wifi.WifiService;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.app.exception.domain.DeviceNotFoundException;
import com.wifi.obs.app.exception.domain.OverLimitException;
import com.wifi.obs.app.web.dto.request.device.PatchDeviceRequest;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.support.WifiServiceEntitySupporter;
import com.wifi.obs.data.mysql.repository.device.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatchDeviceUseCase {

	private final DeviceRepository deviceRepository;

	private final ValidatedMemberService validatedMemberService;

	private final MemberConverter memberConverter;
	private final DeviceConverter deviceConverter;
	private final WifiServiceDeviceConverter wifiServiceDeviceConverter;

	private final IdMatchValidator idMatchValidator;

	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId, PatchDeviceRequest request) {

		Member member = getMember(memberId);

		WifiServiceDevice device = getDevice(request.getDeviceId());

		if (device.isSameService(request.getChangeServiceId())) {
			return;
		}

		WifiService changeTargetService = device.getService();

		idMatchValidator.validate(memberId, changeTargetService.getMemberId());

		checkConstraint(member, changeTargetService.getId());

		device.changeService(changeTargetService.getId());

		deviceRepository.save(deviceConverter.toEntity(device));
	}

	private Member getMember(Long memberId) {
		return memberConverter.from(validatedMemberService.execute(memberId));
	}

	private WifiServiceDevice getDevice(Long deviceId) {
		return wifiServiceDeviceConverter.from(
				deviceRepository
						.findByIdAndDeletedFalse(deviceId)
						.orElseThrow(() -> new DeviceNotFoundException(deviceId)));
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
}
