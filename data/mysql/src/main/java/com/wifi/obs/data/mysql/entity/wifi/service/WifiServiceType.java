package com.wifi.obs.data.mysql.entity.wifi.service;

import lombok.Getter;

@Getter
public enum WifiServiceType {
	IPTIME("Iptime");

	private final String type;

	WifiServiceType(String type) {
		this.type = type;
	}
}
