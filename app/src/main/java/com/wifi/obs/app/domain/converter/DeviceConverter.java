package com.wifi.obs.app.domain.converter;

import com.wifi.obs.app.domain.model.device.Device;
import com.wifi.obs.app.domain.model.device.DeviceType;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.support.WifiServiceEntitySupporter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeviceConverter {

	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;

	public Device from(DeviceEntity source) {
		return Device.builder()
				.id(source.getId())
				.serviceId(source.getWifiService().getId())
				.type(DeviceType.valueOf(source.getType().getType()))
				.mac(source.getMac())
				.build();
	}

	public DeviceEntity toEntity(Device source) {
		return DeviceEntity.builder()
				.id(source.getId())
				.wifiService(wifiServiceEntitySupporter.getWifiServiceIdEntity(source.getServiceId()))
				.type(getType(source))
				.mac(source.getMac())
				.build();
	}

	private static com.wifi.obs.data.mysql.entity.device.DeviceType getType(Device source) {
		return com.wifi.obs.data.mysql.entity.device.DeviceType.valueOf(source.getType().name());
	}
}
