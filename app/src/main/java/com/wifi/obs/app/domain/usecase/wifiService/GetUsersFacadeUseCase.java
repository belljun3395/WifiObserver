package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.converter.WifiServiceConverter;
import com.wifi.obs.app.domain.dto.response.service.OnConnectUserInfos;
import com.wifi.obs.app.domain.dto.response.service.UserInfo;
import com.wifi.obs.app.domain.model.wifi.WifiService;
import com.wifi.obs.app.domain.service.device.BrowseDeviceService;
import com.wifi.obs.app.domain.service.wifi.GetUserService;
import com.wifi.obs.app.domain.usecase.support.manager.GetUserServiceManager;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.app.exception.domain.ClientProblemException;
import com.wifi.obs.app.exception.domain.ServiceNotFoundException;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.support.WifiAuthEntitySupporter;
import com.wifi.obs.data.mysql.entity.support.WifiServiceEntitySupporter;
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

	private final WifiServiceConverter wifiServiceConverter;

	private final IdMatchValidator idMatchValidator;

	private final WifiAuthEntitySupporter wifiAuthEntitySupporter;
	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public OnConnectUserInfos execute(Long memberId, Long sid, Optional<Boolean> filter) {

		WifiService service = getWifiService(sid);

		isOn(service);

		idMatchValidator.validate(memberId, service.getMemberId());

		OnConnectUserInfos res =
				getService(service)
						.execute(wifiAuthEntitySupporter.getReferenceEntity(service.getAuthId()));

		if (!isFiltered(filter)) {
			return res;
		}

		List<DeviceEntity> devices = getDevices(service);

		return getFilteredRes(res, devices);
	}

	private WifiService getWifiService(Long sid) {
		return wifiServiceConverter.from(
				wifiServiceRepository
						.findByIdAndDeletedFalse(sid)
						.orElseThrow(() -> new ServiceNotFoundException(sid)));
	}

	private void isOn(WifiService service) {
		if (!service.isOn()) {
			throw new ClientProblemException();
		}
	}

	private GetUserService getService(WifiService service) {
		return getUserServiceManager.getService(service.getType());
	}

	private boolean isFiltered(Optional<Boolean> filter) {
		if (filter.isEmpty()) {
			return false;
		}

		if (filter.get().equals(Boolean.FALSE)) {
			return false;
		}

		return true;
	}

	private List<DeviceEntity> getDevices(WifiService service) {
		return browseDeviceService.execute(
				wifiServiceEntitySupporter.getReferenceEntity(service.getId()));
	}

	protected OnConnectUserInfos getFilteredRes(OnConnectUserInfos res, List<DeviceEntity> devices) {
		List<String> macSources =
				devices.stream().map(DeviceEntity::getMac).collect(Collectors.toList());

		List<UserInfo> filteredUsers =
				res.getUsers().stream()
						.filter(info -> macSources.contains(info.getMac()))
						.collect(Collectors.toList());
		return OnConnectUserInfos.of(filteredUsers);
	}
}
