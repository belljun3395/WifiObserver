package com.wifi.obs.app.domain.service.device;

import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
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

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(List<WifiServiceEntity> services) {
		for (WifiServiceEntity ws : services) {
			deviceRepository.deleteAllById(
					deviceRepository.findAllByWifiServiceAndDeletedFalse(ws).stream()
							.map(DeviceEntity::getId)
							.collect(Collectors.toList()));
		}
	}

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public void execute(WifiServiceEntity service) {
		deviceRepository.deleteAllById(
				deviceRepository.findAllByWifiServiceAndDeletedFalse(service).stream()
						.map(DeviceEntity::getId)
						.collect(Collectors.toList()));
	}
}
