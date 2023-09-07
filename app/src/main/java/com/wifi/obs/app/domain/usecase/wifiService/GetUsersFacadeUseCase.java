package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.dto.response.service.OnConnectUserInfos;
import com.wifi.obs.app.domain.dto.response.service.UserInfo;
import com.wifi.obs.app.domain.service.device.BrowseDeviceService;
import com.wifi.obs.app.domain.usecase.support.manager.GetUserServiceManager;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.app.exception.domain.ClientProblemException;
import com.wifi.obs.app.exception.domain.ServiceNotFoundException;
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

	private final GetUserServiceManager getUserServiceManager;

	private final IdMatchValidator idMatchValidator;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public OnConnectUserInfos execute(Long memberId, Long sid, Optional<Boolean> filter) {
		WifiServiceEntity service = getService(sid);

		isError(service);

		idMatchValidator.validate(memberId, service.getMember().getId());

		WifiAuthEntity authInfo = service.getWifiAuthEntity();

		WifiServiceType serviceType = service.getServiceType();

		OnConnectUserInfos res = getUserServiceManager.getService(serviceType).execute(authInfo);

		if (filter.isEmpty() || filter.get().equals(Boolean.FALSE)) {
			return res;
		}

		List<DeviceEntity> devices = browseDeviceService.execute(service);
		return getFilteredRes(res, devices);
	}

	private void isError(WifiServiceEntity service) {
		if (service.getStatus().equals(WifiStatus.ERROR)) {
			throw new ClientProblemException();
		}
	}

	private WifiServiceEntity getService(Long sid) {
		return wifiServiceRepository.findById(sid).orElseThrow(() -> new ServiceNotFoundException(sid));
	}

	private OnConnectUserInfos getFilteredRes(OnConnectUserInfos res, List<DeviceEntity> devices) {
		List<UserInfo> filteredUsers =
				res.getUserInfos().stream().filter(devices::contains).collect(Collectors.toList());
		return OnConnectUserInfos.of(filteredUsers);
	}
}
