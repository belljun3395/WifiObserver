package com.wifi.obs.app.domain.model;

import com.wifi.obs.data.mysql.entity.device.DeviceEntity;
import com.wifi.obs.data.mysql.entity.device.DeviceType;
import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class DeviceModel {

	private DeviceEntity source;
	private Long id;
	private DeviceType deviceType;
	private String mac;

	public static DeviceModel of(DeviceEntity source) {
		return DeviceModel.builder()
				.source(source)
				.id(source.getId())
				.deviceType(source.getType())
				.mac(source.getMac())
				.build();
	}

	public WifiAuthEntity getWifiAuthEntity() {
		return source.getWifiService().getWifiAuthEntity();
	}

	public WifiServiceType getServiceType() {
		return source.getWifiService().getServiceType();
	}
}
