package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.converter.DeviceModelConverter;
import com.wifi.obs.app.domain.converter.MemberModelConverter;
import com.wifi.obs.app.domain.converter.WifiServiceModelConverter;
import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.app.domain.model.DeviceModel;
import com.wifi.obs.app.domain.model.MemberModel;
import com.wifi.obs.app.domain.model.WifiServiceModel;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.usecase.support.manager.GetServiceDeviceStetInfoManager;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.app.exception.domain.DeviceNotFoundException;
import com.wifi.obs.app.web.dto.request.StetType;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.support.WifiServiceEntitySupporter;
import com.wifi.obs.data.mysql.repository.device.DeviceRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetDeviceStetFacadeUseCase {

	private final DeviceRepository deviceRepository;

	private final ValidatedMemberService validatedMemberService;
	private final GetServiceDeviceStetInfoManager serviceDeviceStetInfoManager;

	private final DeviceModelConverter deviceModelConverter;
	private final MemberModelConverter memberModelConverter;
	private final WifiServiceModelConverter wifiServiceModelConverter;

	private final IdMatchValidator idMatchValidator;

	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public DeviceStetInfo execute(Long memberId, String mac, StetType type) {

		MemberModel member = memberModelConverter.from(validatedMemberService.execute(memberId));

		DeviceModel device = deviceModelConverter.from(getDevice(mac));

		WifiServiceModel service =
				wifiServiceModelConverter.from(
						wifiServiceEntitySupporter.getReferenceEntity(device.getServiceId()));

		idMatchValidator.validate(member.getId(), service.getMemberId());

		return serviceDeviceStetInfoManager
				.getService(type)
				.execute(deviceModelConverter.toEntity(device), LocalDateTime.now());
	}

	private DeviceEntity getDevice(String mac) {
		return deviceRepository
				.findByMacAndDeletedFalse(mac)
				.orElseThrow(() -> new DeviceNotFoundException(mac));
	}
}
