package com.wifi.obs.data.mysql.entity.wifi.service;

import lombok.Getter;

@Getter
public enum WifiStatus {
	ON("ON"),
	ERROR("ERROR");

	private String type;

	WifiStatus(String type) {
		this.type = type;
	}
}
