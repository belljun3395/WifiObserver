package com.wifi.obs.app.domain.model.wifi;

import lombok.Getter;

@Getter
public enum WifiServiceType {
	IPTIME("IPTIME", "Iptime");

	private final String type;
	private final String key;

	WifiServiceType(String type, String key) {
		this.type = type;
		this.key = key;
	}
}
