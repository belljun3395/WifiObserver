package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.model.MemberModel;
import com.wifi.obs.app.domain.model.WifiServiceModel;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.service.wifi.ValidatedWifiServiceService;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.app.exception.domain.DeviceNotFoundException;
import com.wifi.obs.app.exception.domain.NotMatchInformationException;
import com.wifi.obs.app.exception.domain.OverLimitException;
import com.wifi.obs.app.web.dto.request.device.PatchDeviceRequest;
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
public class PatchDeviceUseCase {

	private final DeviceRepository deviceRepository;

	private final ValidatedMemberService validatedMemberService;
	private final ValidatedWifiServiceService validatedWifiServiceService;

	private final IdMatchValidator idMatchValidator;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId, PatchDeviceRequest request) {

		MemberModel member = MemberModel.of(validatedMemberService.execute(memberId));

		DeviceEntity device = getDevice(request);

		try {
			idMatchValidator.validate(request.getChangeServiceId(), device.getWifiService().getId());
		} catch (NotMatchInformationException nme) {
			return;
		}

		WifiServiceModel changeTargetService =
				WifiServiceModel.of(validatedWifiServiceService.execute(request.getChangeServiceId()));

		idMatchValidator.validate(memberId, changeTargetService.getId());

		validateServiceDeviceCount(changeTargetService.getSource(), member);

		deviceRepository.save(device.toBuilder().wifiService(changeTargetService.getSource()).build());
	}

	private DeviceEntity getDevice(PatchDeviceRequest request) {
		return deviceRepository
				.findById(request.getDeviceId())
				.orElseThrow(() -> new DeviceNotFoundException(request.getDeviceId()));
	}

	private void validateServiceDeviceCount(WifiServiceEntity service, MemberModel member) {
		int savedDeviceCount = deviceRepository.findAllByWifiServiceAndDeletedFalse(service).size();

		if (member.isOverDeviceMaxCount((long) savedDeviceCount)) {
			throw new OverLimitException();
		}
	}
}
