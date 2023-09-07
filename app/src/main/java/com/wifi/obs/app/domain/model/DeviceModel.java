package com.wifi.obs.app.domain.model;

import com.wifi.obs.data.mysql.entity.device.DeviceType;
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

	private Long id;
	private Long serviceId;
	private DeviceType deviceType;
	private String mac;

	public void patchServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
}
