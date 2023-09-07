package com.wifi.obs.app.domain.usecase.device;

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

	private final IdMatchValidator idMatchValidator;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId, SaveDeviceRequest request) {

		WifiServiceModel service =
				WifiServiceModel.of(validatedWifiServiceService.execute(request.getSid()));
		MemberModel member = MemberModel.of(validatedMemberService.execute(memberId));

		idMatchValidator.validate(memberId, service.getMemberId());

		validateMemberServiceDeviceCount(member, service.getSource());
		validateRequestMacDuplication(request.getMac().toUpperCase(), service.getSource());

		deviceRepository.save(
				DeviceEntity.builder()
						.type(request.getDeviceType())
						.mac(request.getMac().toUpperCase())
						.wifiService(service.getSource())
						.build());
	}

	private void validateMemberServiceDeviceCount(MemberModel member, WifiServiceEntity service) {
		int savedDeviceCount = deviceRepository.findAllByWifiServiceAndDeletedFalse(service).size();

		if (member.isOverDeviceMaxCount((long) savedDeviceCount)) {
			throw new OverLimitException();
		}
	}

	private void validateRequestMacDuplication(String mac, WifiServiceEntity service) {
		Boolean isExist = deviceRepository.existsByMacAndWifiServiceAndDeletedFalse(mac, service);
		if (isExist) {
			throw new AlreadyRegisterException();
		}
	}
}
