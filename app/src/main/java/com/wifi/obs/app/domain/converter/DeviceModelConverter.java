package com.wifi.obs.app.domain.converter;

import com.wifi.obs.app.domain.model.DeviceModel;
import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.support.WifiServiceEntitySupporter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeviceModelConverter {

	private final WifiServiceEntitySupporter wifiServiceEntitySupporter;

	public DeviceModel from(DeviceEntity source) {
		return DeviceModel.builder()
				.id(source.getId())
				.serviceId(source.getWifiService().getId())
				.deviceType(source.getType())
				.mac(source.getMac())
				.build();
	}

	public DeviceEntity toEntity(DeviceModel source) {
		return DeviceEntity.builder()
				.id(source.getId())
				.wifiService(wifiServiceEntitySupporter.getWifiServiceIdEntity(source.getServiceId()))
				.type(source.getDeviceType())
				.mac(source.getMac())
				.build();
	}
}
