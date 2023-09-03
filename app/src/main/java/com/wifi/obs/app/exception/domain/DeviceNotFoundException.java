package com.wifi.obs.app.exception.domain;

public class DeviceNotFoundException extends DomainException {

	private String mac;

	private Long deviceId;

	public DeviceNotFoundException(String mac) {
		this.mac = mac;
	}

	public DeviceNotFoundException(Long deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String getMessage() {
		return mac != null ? "존재하지 않는 장치입니다. mac: " + mac : "존재하지 않는 장치입니다. deviceId : " + deviceId;
	}
}
