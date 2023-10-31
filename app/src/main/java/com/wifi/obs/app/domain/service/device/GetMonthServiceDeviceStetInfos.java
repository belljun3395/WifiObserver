package com.wifi.obs.app.domain.service.device;

import com.wifi.obs.app.domain.converter.DeviceConverter;
import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.app.domain.dto.response.service.ServiceDeviceStetInfos;
import com.wifi.obs.app.domain.model.device.Device;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.meta.ConnectHistoryMetaEntity;
import com.wifi.obs.data.mysql.repository.meta.ConnectHistoryMetaRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetMonthServiceDeviceStetInfos implements GetServiceDeviceStetInfos {

	private final ConnectHistoryMetaRepository connectHistoryMetaRepository;
	private final DeviceConverter deviceConverter;

	@Override
	public ServiceDeviceStetInfos execute(
			List<Device> devices, List<DeviceStetInfo> stetInfos, Long sid, LocalDateTime now) {
		List<DeviceEntity> entities = convertToEntities(devices);
		for (DeviceEntity device : entities) {
			Optional<ConnectHistoryMetaEntity> monthStet =
					connectHistoryMetaRepository.findTopByDeviceAndMonthOrderByIdDesc(
							device, (long) now.getMonth().getValue());

			if (monthStet.isEmpty()) {
				DeviceStetInfo stetInfo =
						DeviceStetInfo.builder().id(device.getId()).mac(device.getMac()).build();
				stetInfos.add(stetInfo);
				continue;
			}

			DeviceStetInfo stetInfo =
					DeviceStetInfo.builder()
							.id(device.getId())
							.mac(device.getMac())
							.time(monthStet.get().getConnectedTimeOnMonth())
							.build();
			stetInfos.add(stetInfo);
		}
		return new ServiceDeviceStetInfos(sid, stetInfos);
	}

	private List<DeviceEntity> convertToEntities(List<Device> devices) {
		List<DeviceEntity> entities = new ArrayList<>();
		for (Device device : devices) {
			entities.add(deviceConverter.toEntity(device));
		}
		return entities;
	}
}
