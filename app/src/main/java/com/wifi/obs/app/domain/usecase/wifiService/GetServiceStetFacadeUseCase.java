package com.wifi.obs.app.domain.usecase.wifiService;

import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.app.domain.dto.response.service.ServiceDeviceStetInfos;
import com.wifi.obs.app.domain.service.device.BrowseDeviceService;
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
						.orElseThrow(() -> new RuntimeException("존재하지 않는 서비스입니다."));

		if (service.getStatus().equals(WifiStatus.ERROR)) {
			throw new RuntimeException("서비스에 문제가 있습니다.");
		}

		if (!memberId.equals(service.getMember().getId())) {
			throw new RuntimeException("해당 서비스는 회원의 서비스가 아닙니다.");
		}

		LocalDateTime now = LocalDateTime.now();
		List<DeviceStetInfo> deviceStetInfos = new ArrayList<>();

		List<DeviceEntity> devices = browseDeviceService.execute(service);

		if (type.equals(StetType.MONTH)) {
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

		if (type.equals(StetType.DAY)) {
			for (DeviceEntity device : devices) {
				Optional<ConnectHistoryMetaEntity> dayStet =
						connectHistoryMetaRepository.findTopByDeviceAndDayAndMonthOrderByIdDesc(
								device, (long) now.getDayOfMonth(), (long) now.getMonth().getValue());

				if (dayStet.isEmpty()) {
					DeviceStetInfo stetInfo =
							DeviceStetInfo.builder().id(device.getId()).mac(device.getMac()).build();
					deviceStetInfos.add(stetInfo);
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

		throw new RuntimeException("잘못된 요청입니다.");
	}
}
