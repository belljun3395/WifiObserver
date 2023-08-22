package com.wifi.observer.client.wifi.support.log;

import lombok.Getter;

@Getter
public enum WifiLogProperties {
	WIFI_TRACEID("wifiTraceId"),
	HOST("host"),
	TIME("time"),
	;

	private final String key;

	WifiLogProperties(String key) {
		this.key = key;
	}
}
