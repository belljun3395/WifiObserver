package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.converter.MemberModelConverter;
import com.wifi.obs.app.domain.converter.WifiServiceModelConverter;
import com.wifi.obs.app.domain.model.MemberModel;
import com.wifi.obs.app.domain.model.WifiServiceModel;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.service.wifi.ValidatedWifiServiceService;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.app.exception.domain.AlreadyRegisterException;
import com.wifi.obs.app.exception.domain.OverLimitException;
import com.wifi.obs.app.web.dto.request.device.SaveDeviceRequest;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.support.WifiServiceEntitySupporter;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
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

	private final MemberModelConverter memberModelConverter;
	private final WifiServiceModelConverter wifiServiceModelConverter;

	private final IdMatchValidator idMatchValidator;

	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId, SaveDeviceRequest request) {

		WifiServiceModel service =
				wifiServiceModelConverter.from(validatedWifiServiceService.execute(request.getSid()));

		MemberModel member = memberModelConverter.from(validatedMemberService.execute(memberId));

		idMatchValidator.validate(memberId, service.getMemberId());

		validateMemberServiceDeviceCount(member, service.getId());
		validateRequestMacDuplication(request.getMac().toUpperCase(), service.getId());

		deviceRepository.save(
				DeviceEntity.builder()
						.type(request.getDeviceType())
						.mac(request.getMac().toUpperCase())
						.wifiService(wifiServiceEntitySupporter.getReferenceEntity(service.getId()))
						.build());
	}

	private void validateMemberServiceDeviceCount(MemberModel member, Long serviceId) {
		WifiServiceEntity service = wifiServiceEntitySupporter.getReferenceEntity(serviceId);

		int savedDeviceCount = deviceRepository.findAllByWifiServiceAndDeletedFalse(service).size();

		if (member.isOverDeviceMaxCount((long) savedDeviceCount)) {
			throw new OverLimitException();
		}
	}

	private void validateRequestMacDuplication(String mac, Long serviceId) {
		WifiServiceEntity service = wifiServiceEntitySupporter.getReferenceEntity(serviceId);

		Boolean isExist = deviceRepository.existsByMacAndWifiServiceAndDeletedFalse(mac, service);

		if (isExist) {
			throw new AlreadyRegisterException();
		}
	}
}
