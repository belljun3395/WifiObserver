package com.wifi.obs.app.domain.converter;

import com.wifi.obs.app.domain.model.ModelId;
import com.wifi.obs.app.domain.model.device.DeviceType;
import com.wifi.obs.app.domain.model.device.WifiServiceDevice;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WifiServiceDeviceConverter {

	private final WifiServiceConverter wifiServiceConverter;

	public WifiServiceDevice from(DeviceEntity source) {
		return WifiServiceDevice.builder()
				.service(wifiServiceConverter.from(source.getWifiService()))
				.id(ModelId.of(source.getId()))
				.serviceId(ModelId.of(source.getWifiService().getId()))
				.type(DeviceType.valueOf(source.getType().getType()))
				.mac(source.getMac())
				.build();
	}
}
