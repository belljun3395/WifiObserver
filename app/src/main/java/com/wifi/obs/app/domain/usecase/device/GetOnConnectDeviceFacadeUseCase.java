package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.converter.MemberConverter;
import com.wifi.obs.app.domain.converter.WifiServiceDeviceConverter;
import com.wifi.obs.app.domain.dto.response.device.DeviceOnConnectInfo;
import com.wifi.obs.app.domain.dto.response.service.OnConnectUserInfos;
import com.wifi.obs.app.domain.dto.response.service.UserInfo;
import com.wifi.obs.app.domain.model.device.Device;
import com.wifi.obs.app.domain.model.device.WifiServiceDevice;
import com.wifi.obs.app.domain.model.member.Member;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.service.wifi.GetUserService;
import com.wifi.obs.app.domain.usecase.support.manager.GetUserServiceManager;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.app.exception.domain.DeviceNotFoundException;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.support.WifiAuthEntitySupporter;
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

	private final MemberConverter memberConverter;
	private final WifiServiceDeviceConverter wifiServiceDeviceConverter;

	private final IdMatchValidator idMatchValidator;

	private final WifiAuthEntitySupporter wifiAuthEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public DeviceOnConnectInfo execute(Long memberId, String mac) {

		Member member = getMember(memberId);

		WifiServiceDevice device = getDevice(mac);

		idMatchValidator.validate(member.getId(), device.getMemberId());

		GetUserService getUserService = getService(device);

		return getDeviceOnConnectInfo(getUserService, mac, device);
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

	private GetUserService getService(WifiServiceDevice device) {
		return getUserServiceManager.getService(device.getServiceType());
	}

	protected DeviceOnConnectInfo getDeviceOnConnectInfo(
			GetUserService service, String mac, WifiServiceDevice device) {

		OnConnectUserInfos users = getUsers(service, device);

		Optional<String> filteredUsers = getFilteredUsers(mac, users);

		if (filteredUsers.isEmpty()) {
			return getDeviceOnConnectInfo(device, false);
		}

		return getDeviceOnConnectInfo(device, true);
	}

	private OnConnectUserInfos getUsers(GetUserService service, WifiServiceDevice device) {
		return service.execute(
				wifiAuthEntitySupporter.getWifiAuthIdEntity(device.getService().getAuthId()));
	}

	protected Optional<String> getFilteredUsers(String mac, OnConnectUserInfos users) {
		return users.getUsers().stream().map(UserInfo::getMac).filter(mac::equals).findFirst();
	}

	private DeviceOnConnectInfo getDeviceOnConnectInfo(Device device, Boolean connected) {
		return DeviceOnConnectInfo.builder()
				.id(device.getId())
				.mac(device.getMac())
				.connected(connected)
				.build();
	}
}
