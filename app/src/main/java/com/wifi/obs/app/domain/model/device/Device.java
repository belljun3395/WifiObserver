package com.wifi.obs.app.domain.model.device;

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
public class Device {

	private Long id;
	private Long serviceId;
	private DeviceType type;
	private String mac;

	protected void changeServiceId(Long sid) {
		this.serviceId = sid;
	}
}
