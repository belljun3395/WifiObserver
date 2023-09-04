package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.app.domain.dto.response.service.ServiceDeviceStetInfos;
import com.wifi.obs.app.domain.service.device.BrowseDeviceService;
import com.wifi.obs.app.domain.service.device.GetServiceDeviceStetInfos;
import com.wifi.obs.app.exception.domain.BadTypeRequestException;
import com.wifi.obs.app.exception.domain.ClientProblemException;
import com.wifi.obs.app.exception.domain.NotMatchInformationException;
import com.wifi.obs.app.exception.domain.ServiceNotFoundException;
import com.wifi.obs.app.web.dto.request.StetType;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiStatus;
import com.wifi.obs.data.mysql.repository.wifi.service.WifiServiceRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetServiceStetFacadeUseCase {

	private final WifiServiceRepository wifiServiceRepository;

	private final BrowseDeviceService browseDeviceService;
	private final Map<String, GetServiceDeviceStetInfos> getServiceDeviceStetInfosMap;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public ServiceDeviceStetInfos execute(Long memberId, Long serviceId, StetType type) {
		WifiServiceEntity service =
				wifiServiceRepository
						.findById(serviceId)
						.orElseThrow(() -> new ServiceNotFoundException(serviceId));

		if (service.getStatus().equals(WifiStatus.ERROR)) {
			throw new ClientProblemException();
		}

		if (!memberId.equals(service.getMember().getId())) {
			throw new NotMatchInformationException();
		}

		LocalDateTime now = LocalDateTime.now();
		List<DeviceStetInfo> deviceStetInfos = new ArrayList<>();

		List<DeviceEntity> devices = browseDeviceService.execute(service);

		GetServiceDeviceStetInfos getServiceDeviceStetInfos = getMatchStetTypDeviceStetInfos(type);

		return getServiceDeviceStetInfos.execute(devices, deviceStetInfos, serviceId, now);
	}

	private GetServiceDeviceStetInfos getMatchStetTypDeviceStetInfos(StetType type) {
		String key =
				getServiceDeviceStetInfosMap.keySet().stream()
						.filter(s -> s.contains(type.getType()))
						.findFirst()
						.orElseThrow(BadTypeRequestException::new);

		return getServiceDeviceStetInfosMap.get(key);
	}
}
