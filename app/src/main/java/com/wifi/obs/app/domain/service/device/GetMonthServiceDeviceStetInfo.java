package com.wifi.obs.app.domain.service.device;

import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.meta.ConnectHistoryMetaEntity;
import com.wifi.obs.data.mysql.repository.meta.ConnectHistoryMetaRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetMonthServiceDeviceStetInfo implements GetServiceDeviceStetInfo {

	private final ConnectHistoryMetaRepository connectHistoryMetaRepository;

	@Override
	public DeviceStetInfo execute(DeviceEntity device, LocalDateTime now) {
		Optional<ConnectHistoryMetaEntity> monthStet =
				connectHistoryMetaRepository.findTopByDeviceAndMonthOrderByIdDesc(
						device, (long) now.getMonth().getValue());

		if (monthStet.isEmpty()) {
			return getDeviceStetInfo(device, Optional.empty());
		}
		return getDeviceStetInfo(device, Optional.of(monthStet.get().getConnectedTimeOnMonth()));
	}

	private DeviceStetInfo getDeviceStetInfo(DeviceEntity device, Optional<Long> connectedTime) {

		if (connectedTime.isEmpty()) {
			return DeviceStetInfo.builder().id(device.getId()).mac(device.getMac()).build();
		}

		return DeviceStetInfo.builder()
				.id(device.getId())
				.mac(device.getMac())
				.time(connectedTime.get())
				.build();
	}
}
