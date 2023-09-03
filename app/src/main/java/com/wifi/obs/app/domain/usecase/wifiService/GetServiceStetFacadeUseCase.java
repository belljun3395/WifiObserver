package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.app.domain.dto.response.service.ServiceDeviceStetInfos;
import com.wifi.obs.app.domain.service.device.BrowseDeviceService;
import com.wifi.obs.app.exception.domain.BadTypeRequestException;
import com.wifi.obs.app.exception.domain.ClientProblemException;
import com.wifi.obs.app.exception.domain.NotMatchInformationException;
import com.wifi.obs.app.exception.domain.ServiceNotFoundException;
import com.wifi.obs.app.web.dto.request.StetType;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.meta.ConnectHistoryMetaEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiStatus;
import com.wifi.obs.data.mysql.repository.meta.ConnectHistoryMetaRepository;
import com.wifi.obs.data.mysql.repository.wifi.service.WifiServiceRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetServiceStetFacadeUseCase {

	private final WifiServiceRepository wifiServiceRepository;
	private final ConnectHistoryMetaRepository connectHistoryMetaRepository;

	private final BrowseDeviceService browseDeviceService;

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

		if (type.equals(StetType.MONTH)) {
			return getMonthServiceDeviceStetInfos(devices, deviceStetInfos, serviceId, now);
		}

		if (type.equals(StetType.DAY)) {
			return getDayServiceDeviceStetInfos(devices, deviceStetInfos, serviceId, now);
		}

		throw new BadTypeRequestException();
	}

	private ServiceDeviceStetInfos getMonthServiceDeviceStetInfos(
			List<DeviceEntity> devices,
			List<DeviceStetInfo> deviceStetInfos,
			Long serviceId,
			LocalDateTime now) {
		for (DeviceEntity device : devices) {
			Optional<ConnectHistoryMetaEntity> monthStet =
					connectHistoryMetaRepository.findTopByDeviceAndMonthOrderByIdDesc(
							device, (long) now.getMonth().getValue());

			if (monthStet.isEmpty()) {
				DeviceStetInfo stetInfo =
						DeviceStetInfo.builder().id(device.getId()).mac(device.getMac()).build();
				deviceStetInfos.add(stetInfo);
				continue;
			}

			DeviceStetInfo stetInfo =
					DeviceStetInfo.builder()
							.id(device.getId())
							.mac(device.getMac())
							.time(monthStet.get().getConnectedTimeOnMonth())
							.build();
			deviceStetInfos.add(stetInfo);
		}
		return new ServiceDeviceStetInfos(serviceId, deviceStetInfos);
	}

	private ServiceDeviceStetInfos getDayServiceDeviceStetInfos(
			List<DeviceEntity> devices,
			List<DeviceStetInfo> deviceStetInfos,
			Long serviceId,
			LocalDateTime now) {
		for (DeviceEntity device : devices) {
			Optional<ConnectHistoryMetaEntity> dayStet =
					connectHistoryMetaRepository.findTopByDeviceAndDayAndMonthOrderByIdDesc(
							device, (long) now.getDayOfMonth(), (long) now.getMonth().getValue());

			if (dayStet.isEmpty()) {
				DeviceStetInfo stetInfo =
						DeviceStetInfo.builder().id(device.getId()).mac(device.getMac()).build();
				deviceStetInfos.add(stetInfo);
				continue;
			}

			DeviceStetInfo stetInfo =
					DeviceStetInfo.builder()
							.id(device.getId())
							.mac(device.getMac())
							.time(dayStet.get().getConnectedTimeOnDay())
							.build();
			deviceStetInfos.add(stetInfo);
		}
		return new ServiceDeviceStetInfos(serviceId, deviceStetInfos);
	}
}
