package com.wifi.obs.data.mysql.entity.device;

import lombok.Getter;

@Getter
public enum DeviceType {
	NOTEBOOK("NOTEBOOK");

	private String type;

	DeviceType(String type) {
		this.type = type;
	}
}
