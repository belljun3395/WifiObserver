package com.wifi.obs.app.domain.service.device;

import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.app.domain.dto.response.service.ServiceDeviceStetInfos;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.meta.ConnectHistoryMetaEntity;
import com.wifi.obs.data.mysql.repository.meta.ConnectHistoryMetaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetDayServiceDeviceStetInfos implements GetServiceDeviceStetInfos {

	private final ConnectHistoryMetaRepository connectHistoryMetaRepository;

	@Override
	public ServiceDeviceStetInfos execute(
			List<DeviceEntity> devices, List<DeviceStetInfo> stetInfos, Long sid, LocalDateTime now) {
		for (DeviceEntity device : devices) {
			Optional<ConnectHistoryMetaEntity> dayStet =
					connectHistoryMetaRepository.findTopByDeviceAndDayAndMonthOrderByIdDesc(
							device, (long) now.getDayOfMonth(), (long) now.getMonth().getValue());

			if (dayStet.isEmpty()) {
				DeviceStetInfo stetInfo =
						DeviceStetInfo.builder().id(device.getId()).mac(device.getMac()).build();
				stetInfos.add(stetInfo);
				continue;
			}

			DeviceStetInfo stetInfo =
					DeviceStetInfo.builder()
							.id(device.getId())
							.mac(device.getMac())
							.time(dayStet.get().getConnectedTimeOnDay())
							.build();
			stetInfos.add(stetInfo);
		}
		return new ServiceDeviceStetInfos(sid, stetInfos);
	}
}
