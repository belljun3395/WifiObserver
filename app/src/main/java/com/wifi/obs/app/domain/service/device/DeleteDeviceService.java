package com.wifi.obs.app.domain.service.device;

import com.wifi.obs.app.domain.model.wifi.WifiService;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.support.WifiServiceEntitySupporter;
import com.wifi.obs.data.mysql.repository.device.DeviceRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteDeviceService {

	private final DeviceRepository deviceRepository;

	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(List<WifiService> services) {
		for (WifiService ws : services) {
			deviceRepository.deleteAllById(
					deviceRepository
							.findAllByWifiServiceAndDeletedFalse(
									wifiServiceEntitySupporter.getWifiServiceIdEntity(ws.getId()))
							.stream()
							.map(DeviceEntity::getId)
							.collect(Collectors.toList()));
		}
	}

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(WifiService service) {
		deviceRepository.deleteAllById(
				deviceRepository
						.findAllByWifiServiceAndDeletedFalse(
								wifiServiceEntitySupporter.getWifiServiceIdEntity(service.getId()))
						.stream()
						.map(DeviceEntity::getId)
						.collect(Collectors.toList()));
	}
}
