package com.wifi.obs.app.domain.usecase.device;

import com.wifi.obs.app.domain.dto.response.device.DeviceOnConnectInfo;
import com.wifi.obs.app.domain.dto.response.service.OnConnectUserInfos;
import com.wifi.obs.app.domain.dto.response.service.UserInfo;
import com.wifi.obs.app.domain.service.member.ValidatedMemberService;
import com.wifi.obs.app.domain.service.wifi.iptime.GetIptimeUsersService;
import com.wifi.obs.app.exception.domain.BadTypeRequestException;
import com.wifi.obs.app.exception.domain.DeviceNotFoundException;
import com.wifi.obs.app.exception.domain.NotMatchInformationException;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceType;
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
	private final GetIptimeUsersService getIptimeUsersService;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public DeviceOnConnectInfo execute(Long memberId, String mac) {
		MemberEntity member = validatedMemberService.execute(memberId);

		DeviceEntity device =
				deviceRepository
						.findByMacAndDeletedFalse(mac)
						.orElseThrow(() -> new DeviceNotFoundException(mac));

		if (!member.getId().equals(device.getWifiService().getMember().getId())) {
			throw new NotMatchInformationException();
		}

		WifiAuthEntity auth = device.getWifiService().getWifiAuthEntity();

		if (device.getWifiService().getServiceType().equals(WifiServiceType.IPTIME)) {
			return getIptimeDeviceOnConnectInfo(mac, device, auth);
		}

		throw new BadTypeRequestException();
	}

	private DeviceOnConnectInfo getIptimeDeviceOnConnectInfo(
			String mac, DeviceEntity device, WifiAuthEntity auth) {
		OnConnectUserInfos users = getIptimeUsersService.execute(auth);

		Optional<String> filteredMac =
				users.getUserInfos().stream().map(UserInfo::getMac).filter(mac::equals).findFirst();
		if (filteredMac.isEmpty()) {
			return getDeviceOnConnectInfo(device, false);
		}

		return getDeviceOnConnectInfo(device, true);
	}

	private DeviceOnConnectInfo getDeviceOnConnectInfo(DeviceEntity device, Boolean connected) {
		return DeviceOnConnectInfo.builder()
				.id(device.getId())
				.mac(device.getMac())
				.connected(connected)
				.build();
	}
}
