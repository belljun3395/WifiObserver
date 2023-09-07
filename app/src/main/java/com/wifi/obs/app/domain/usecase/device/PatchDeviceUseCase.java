package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.converter.DeviceModelConverter;
import com.wifi.obs.app.domain.converter.MemberModelConverter;
import com.wifi.obs.app.domain.converter.WifiServiceModelConverter;
import com.wifi.obs.app.domain.model.DeviceModel;
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
public class PatchDeviceUseCase {

	private final DeviceRepository deviceRepository;

	private final ValidatedMemberService validatedMemberService;
	private final ValidatedWifiServiceService validatedWifiServiceService;

	private final MemberModelConverter memberModelConverter;
	private final DeviceModelConverter deviceModelConverter;
	private final WifiServiceModelConverter wifiServiceModelConverter;

	private final IdMatchValidator idMatchValidator;

	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(Long memberId, PatchDeviceRequest request) {

		MemberModel member = memberModelConverter.from(validatedMemberService.execute(memberId));

		DeviceModel device = deviceModelConverter.from(getDevice(request));

		try {
			idMatchValidator.validate(request.getChangeServiceId(), device.getServiceId());
		} catch (NotMatchInformationException nme) {
			return;
		}

		WifiServiceModel changeTargetService =
				wifiServiceModelConverter.from(
						validatedWifiServiceService.execute(request.getChangeServiceId()));

		idMatchValidator.validate(memberId, changeTargetService.getMemberId());

		validateServiceDeviceCount(changeTargetService.getId(), member);

		device.patchServiceId(changeTargetService.getId());

		deviceRepository.save(deviceModelConverter.toEntity(device));
	}

	private DeviceEntity getDevice(PatchDeviceRequest request) {
		return deviceRepository
				.findByIdAndDeletedFalse(request.getDeviceId())
				.orElseThrow(() -> new DeviceNotFoundException(request.getDeviceId()));
	}

	private void validateServiceDeviceCount(Long serviceId, MemberModel member) {
		WifiServiceEntity service = wifiServiceEntitySupporter.getReferenceEntity(serviceId);

		int savedDeviceCount = deviceRepository.findAllByWifiServiceAndDeletedFalse(service).size();

		if (member.isOverDeviceMaxCount((long) savedDeviceCount)) {
			throw new OverLimitException();
		}
	}
}
