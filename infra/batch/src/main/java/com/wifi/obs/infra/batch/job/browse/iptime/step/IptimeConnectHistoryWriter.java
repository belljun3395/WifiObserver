package com.wifi.obs.infra.batch.job.browse.iptime.step;

import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.history.ConnectHistoryEntity;
import com.wifi.obs.data.mysql.entity.history.DisConnectHistoryEntity;
import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.obs.data.mysql.repository.device.DeviceRepository;
import com.wifi.obs.data.mysql.repository.history.connect.ConnectHistoryRepository;
import com.wifi.obs.data.mysql.repository.history.disConnect.DisConnectHistoryRepository;
import com.wifi.obs.data.mysql.repository.wifi.auth.WifiAuthRepository;
import com.wifi.obs.data.mysql.repository.wifi.service.WifiServiceRepository;
import com.wifi.obs.infra.batch.service.SaveHistoryMeatService;
import com.wifi.observer.client.wifi.dto.response.OnConnectUserInfo;
import com.wifi.observer.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeConnectHistoryWriter implements ItemWriter<IptimeOnConnectUserInfosResponse> {

	private final WifiAuthRepository wifiAuthRepository;
	private final WifiServiceRepository wifiServiceRepository;
	private final DeviceRepository deviceRepository;

	private final SaveHistoryMeatService saveHistoryMeatService;

	private final ConnectHistoryRepository connectHistoryRepository;
	private final DisConnectHistoryRepository disConnectHistoryRepository;

	@Override
	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void write(List<? extends IptimeOnConnectUserInfosResponse> items) {
		log.debug(">>> writer items : {}", items);

		List<WifiAuthEntity> auths = getAuths(items);

		List<WifiServiceEntity> services = getServices(auths);

		LocalDateTime now = LocalDateTime.now();

		for (int i = 0; i < items.size(); i++) {
			IptimeOnConnectUserInfosResponse response = items.get(i);
			log.debug(">>> response : {}", response);

			WifiServiceEntity service = services.get(i);
			log.debug(">>> service : {}", service);

			List<DeviceEntity> serviceDevices =
					deviceRepository.findAllByWifiServiceAndDeletedFalse(service);
			log.debug(">>> serviceDevices : {}", serviceDevices);

			List<String> connectedDevices =
					Objects.requireNonNull(response.getResponse().orElse(null)).getUsers().stream()
							.map(OnConnectUserInfo::getUser)
							.collect(Collectors.toList());
			log.debug(">>> connectedDevices : {}", connectedDevices);

			List<DeviceEntity> filteredConnectedDevices =
					serviceDevices.stream()
							.filter(device -> connectedDevices.contains(device.getMac()))
							.collect(Collectors.toList());
			log.debug(">>> filteredConnectedDevices : {}", filteredConnectedDevices);

			List<ConnectHistoryEntity> hasConnectHistories =
					connectHistoryRepository.findAllByWifiServiceAndCreateAtAfterAndDeletedFalse(
							service,
							LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0));

			List<DeviceEntity> hasConnectHistoryDevices =
					hasConnectHistories.stream()
							.map(ConnectHistoryEntity::getDevice)
							.collect(Collectors.toList());
			log.debug(">>> hasConnectHistoryDevices : {}", hasConnectHistoryDevices);

			log.debug("=== saveIfFilteredConnectedDeviceHasNoHistory ===");
			for (DeviceEntity device : filteredConnectedDevices) {
				saveIfFilteredConnectedDeviceHasNoHistory(device, hasConnectHistoryDevices, service);
			}

			List<ConnectHistoryEntity> disConnectDevicesHistories =
					hasConnectHistories.stream()
							.filter(d -> !filteredConnectedDevices.contains(d.getDevice()))
							.collect(Collectors.toList());
			log.debug(">>> disConnectDevicesHistories : {}", disConnectDevicesHistories);

			for (ConnectHistoryEntity disConnectDevicesHistory : disConnectDevicesHistories) {
				Long durationTime = getDurationTime(disConnectDevicesHistory.getStartTime(), now);

				log.debug("=== saveDisConnectHistory ===");
				disConnectHistoryRepository.save(
						DisConnectHistoryEntity.builder()
								.endTime(now)
								.device(disConnectDevicesHistory.getDevice())
								.wifiService(service)
								.build());

				log.debug("=== saveHistoryMeta ===");
				saveHistoryMeatService.execute(disConnectDevicesHistory, service, now, durationTime);

				log.debug("=== deleteConnectHistory ===");
				connectHistoryRepository.deleteById(disConnectDevicesHistory.getId());
			}
		}
	}

	private List<WifiAuthEntity> getAuths(List<? extends IptimeOnConnectUserInfosResponse> items) {
		return items.stream()
				.map(
						response ->
								wifiAuthRepository.findByHostAndDeletedFalse(response.getHost()).orElse(null))
				.collect(Collectors.toList());
	}

	private List<WifiServiceEntity> getServices(List<WifiAuthEntity> auths) {
		return auths.stream()
				.map(auth -> wifiServiceRepository.findByWifiAuthEntityAndDeletedFalse(auth).get())
				.collect(Collectors.toList());
	}

	private Long getDurationTime(LocalDateTime startTime, LocalDateTime nowTime) {
		int hour = nowTime.getHour() - startTime.getHour();
		int minute = nowTime.getMinute() - startTime.getMinute();
		return (long) (hour * 60 + minute);
	}

	private void saveIfFilteredConnectedDeviceHasNoHistory(
			DeviceEntity filteredConnectedDevice,
			List<DeviceEntity> hasConnectHistoryDevices,
			WifiServiceEntity service) {
		if (!hasConnectHistoryDevices.contains(filteredConnectedDevice)) {
			connectHistoryRepository.save(
					ConnectHistoryEntity.builder()
							.device(filteredConnectedDevice)
							.wifiService(service)
							.startTime(LocalDateTime.now())
							.build());
		}
	}
}
