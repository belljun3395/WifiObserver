package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.converter.DeviceConverter;
import com.wifi.obs.app.domain.converter.MemberConverter;
import com.wifi.obs.app.domain.converter.WifiServiceDeviceConverter;
import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.app.domain.model.device.WifiServiceDevice;
import com.wifi.obs.app.domain.model.member.Member;
import com.wifi.obs.app.domain.service.device.GetServiceDeviceStetInfo;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.usecase.support.manager.GetServiceDeviceStetInfoManager;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.app.exception.domain.DeviceNotFoundException;
import com.wifi.obs.app.web.dto.request.StetType;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
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

	private final MemberConverter memberConverter;
	private final WifiServiceDeviceConverter wifiServiceDeviceConverter;
	private final DeviceConverter deviceConverter;

	private final IdMatchValidator idMatchValidator;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public DeviceStetInfo execute(Long memberId, String mac, StetType type) {

		Member member = getMember(memberId);

		WifiServiceDevice device = getDevice(mac);

		idMatchValidator.validate(member.getId(), device.getMemberId());

		GetServiceDeviceStetInfo service = getService(type);

		return service.execute(deviceConverter.toEntity(device), LocalDateTime.now());
	}

	private Member getMember(Long memberId) {
		return memberConverter.from(validatedMemberService.execute(memberId));
	}

	private WifiServiceDevice getDevice(String mac) {
		return wifiServiceDeviceConverter.from(
				deviceRepository
						.findByMacAndDeletedFalse(mac)
						.orElseThrow(() -> new DeviceNotFoundException(mac)));
	}

	private GetServiceDeviceStetInfo getService(StetType type) {
		return serviceDeviceStetInfoManager.getService(type);
	}
}
