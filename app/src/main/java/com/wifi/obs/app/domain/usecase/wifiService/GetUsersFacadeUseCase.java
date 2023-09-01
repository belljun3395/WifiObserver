package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.dto.response.service.OnConnectUserInfos;
import com.wifi.obs.app.domain.dto.response.service.UserInfo;
import com.wifi.obs.app.domain.service.device.BrowseDeviceService;
import com.wifi.obs.app.domain.service.wifi.iptime.GetIptimeUsersService;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceType;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiStatus;
import com.wifi.obs.data.mysql.repository.wifi.service.WifiServiceRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUsersFacadeUseCase {

	private final WifiServiceRepository wifiServiceRepository;

	private final BrowseDeviceService browseDeviceService;
	private final GetIptimeUsersService getIptimeUsersService;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public OnConnectUserInfos execute(Long memberId, Long serviceId, Optional<Boolean> filter) {
		WifiServiceEntity service =
				wifiServiceRepository
						.findById(serviceId)
						.orElseThrow(() -> new RuntimeException("존재하지 않는 서비스입니다."));

		if (service.getStatus().equals(WifiStatus.ERROR)) {
			throw new RuntimeException("서비스에 문제가 있습니다.");
		}

		if (!memberId.equals(service.getMember().getId())) {
			throw new RuntimeException("해당 서비스는 회원의 서비스가 아닙니다.");
		}

		WifiAuthEntity authInfo = service.getWifiAuthEntity();

		WifiServiceType serviceType = service.getServiceType();

		if (serviceType.equals(WifiServiceType.IPTIME)) {
			OnConnectUserInfos res = getIptimeUsersService.execute(authInfo);

			if (filter.isEmpty() || filter.get().equals(Boolean.FALSE)) {
				return res;
			}

			List<DeviceEntity> devices = browseDeviceService.execute(service);
			return getFilteredRes(res, devices);
		}

		throw new RuntimeException("지원하지 않는 서비스입니다.");
	}

	private OnConnectUserInfos getFilteredRes(OnConnectUserInfos res, List<DeviceEntity> devices) {
		List<UserInfo> filteredUsers =
				res.getUserInfos().stream().filter(devices::contains).collect(Collectors.toList());
		return new OnConnectUserInfos(filteredUsers);
	}
}