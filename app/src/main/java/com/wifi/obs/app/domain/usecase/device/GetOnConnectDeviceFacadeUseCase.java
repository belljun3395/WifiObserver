package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.converter.DeviceModelConverter;
import com.wifi.obs.app.domain.converter.MemberModelConverter;
import com.wifi.obs.app.domain.converter.WifiServiceModelConverter;
import com.wifi.obs.app.domain.dto.response.device.DeviceOnConnectInfo;
import com.wifi.obs.app.domain.dto.response.service.OnConnectUserInfos;
import com.wifi.obs.app.domain.dto.response.service.UserInfo;
import com.wifi.obs.app.domain.model.DeviceModel;
import com.wifi.obs.app.domain.model.MemberModel;
import com.wifi.obs.app.domain.model.WifiServiceModel;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.service.wifi.GetUserService;
import com.wifi.obs.app.domain.usecase.support.manager.GetUserServiceManager;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.app.exception.domain.DeviceNotFoundException;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.support.WifiAuthEntitySupporter;
import com.wifi.obs.data.mysql.entity.support.WifiServiceEntitySupporter;
import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import com.wifi.obs.data.mysql.repository.device.DeviceRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetOnConnectDeviceFacadeUseCase {

	private final DeviceRepository deviceRepository;

	private final ValidatedMemberService validatedMemberService;
	private final GetUserServiceManager getUserServiceManager;

	private final DeviceModelConverter deviceModelConverter;
	private final MemberModelConverter memberModelConverter;
	private final WifiServiceModelConverter wifiServiceModelConverter;

	private final IdMatchValidator idMatchValidator;

	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;
	private final WifiAuthEntitySupporter wifiAuthEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public DeviceOnConnectInfo execute(Long memberId, String mac) {

		MemberModel member = memberModelConverter.from(validatedMemberService.execute(memberId));

		DeviceModel device = deviceModelConverter.from(getDevice(mac));

		WifiServiceModel service =
				wifiServiceModelConverter.from(
						wifiServiceEntitySupporter.getReferenceEntity(device.getServiceId()));

		idMatchValidator.validate(member.getId(), service.getMemberId());

		GetUserService getUserService = getUserServiceManager.getService(service.getServiceType());

		return getDeviceOnConnectInfo(
				getUserService,
				mac,
				deviceModelConverter.toEntity(device),
				wifiAuthEntitySupporter.getReferenceEntity(service.getAuthId()));
	}

	private DeviceEntity getDevice(String mac) {
		return deviceRepository
				.findByMacAndDeletedFalse(mac)
				.orElseThrow(() -> new DeviceNotFoundException(mac));
	}

	protected DeviceOnConnectInfo getDeviceOnConnectInfo(
			GetUserService service, String mac, DeviceEntity device, WifiAuthEntity auth) {

		OnConnectUserInfos users = service.execute(auth);

		Optional<String> filteredMac = getFilteredMac(mac, users);
		if (filteredMac.isEmpty()) {
			return getDeviceOnConnectInfo(device, false);
		}

		return getDeviceOnConnectInfo(device, true);
	}

	protected Optional<String> getFilteredMac(String mac, OnConnectUserInfos users) {
		return users.getUserInfos().stream().map(UserInfo::getMac).filter(mac::equals).findFirst();
	}

	private DeviceOnConnectInfo getDeviceOnConnectInfo(DeviceEntity device, Boolean connected) {
		return DeviceOnConnectInfo.builder()
				.id(device.getId())
				.mac(device.getMac())
				.connected(connected)
				.build();
	}
}
