package com.wifi.obs.app.domain.model.wifi;

import lombok.Getter;

@Getter
public enum WifiStatus {
	ON("ON"),
	ERROR("ERROR");

	private String type;

	WifiStatus(String type) {
		this.type = type;
	}

	public boolean isOn() {
		return this.equals(WifiStatus.ON);
	}
}
