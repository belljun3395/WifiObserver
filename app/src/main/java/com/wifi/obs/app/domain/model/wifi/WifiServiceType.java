package com.wifi.obs.app.domain.model.wifi;

import lombok.Getter;

@Getter
public enum WifiServiceType {
	IPTIME("IPTIME");

	private final String type;

	WifiServiceType(String type) {
		this.type = type;
	}
}
