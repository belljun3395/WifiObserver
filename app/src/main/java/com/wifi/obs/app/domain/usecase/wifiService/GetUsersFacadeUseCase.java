package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.converter.WifiServiceModelConverter;
import com.wifi.obs.app.domain.dto.response.service.OnConnectUserInfos;
import com.wifi.obs.app.domain.dto.response.service.UserInfo;
import com.wifi.obs.app.domain.model.WifiServiceModel;
import com.wifi.obs.app.domain.service.device.BrowseDeviceService;
import com.wifi.obs.app.domain.usecase.support.manager.GetUserServiceManager;
import com.wifi.obs.app.domain.usecase.util.validator.IdMatchValidator;
import com.wifi.obs.app.exception.domain.ClientProblemException;
import com.wifi.obs.app.exception.domain.ServiceNotFoundException;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.support.WifiAuthEntitySupporter;
import com.wifi.obs.data.mysql.entity.support.WifiServiceEntitySupporter;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceType;
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

	private final WifiServiceModelConverter wifiServiceModelConverter;

	private final IdMatchValidator idMatchValidator;

	private final WifiAuthEntitySupporter wifiAuthEntitySupporter;
	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public OnConnectUserInfos execute(Long memberId, Long sid, Optional<Boolean> filter) {
		WifiServiceModel service = wifiServiceModelConverter.from(getService(sid));

		isError(service);

		idMatchValidator.validate(memberId, service.getMemberId());

		WifiServiceType serviceType = service.getServiceType();

		OnConnectUserInfos res =
				getUserServiceManager
						.getService(serviceType)
						.execute(wifiAuthEntitySupporter.getReferenceEntity(service.getAuthId()));

		if (filter.isEmpty() || filter.get().equals(Boolean.FALSE)) {
			return res;
		}

		List<DeviceEntity> devices =
				browseDeviceService.execute(wifiServiceEntitySupporter.getReferenceEntity(service.getId()));
		return getFilteredRes(res, devices);
	}

	private void isError(WifiServiceModel service) {
		if (!service.isOn()) {
			throw new ClientProblemException();
		}
	}

	private WifiServiceEntity getService(Long sid) {
		return wifiServiceRepository
				.findByIdAndDeletedFalse(sid)
				.orElseThrow(() -> new ServiceNotFoundException(sid));
	}

	private OnConnectUserInfos getFilteredRes(OnConnectUserInfos res, List<DeviceEntity> devices) {
		List<String> macSources =
				devices.stream().map(DeviceEntity::getMac).collect(Collectors.toList());

		List<UserInfo> filteredUsers =
				res.getUserInfos().stream()
						.filter(info -> macSources.contains(info.getMac()))
						.collect(Collectors.toList());
		return OnConnectUserInfos.of(filteredUsers);
	}
}
