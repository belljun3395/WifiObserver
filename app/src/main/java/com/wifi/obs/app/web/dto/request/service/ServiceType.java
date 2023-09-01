package com.wifi.obs.app.web.dto.request.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum ServiceType {
	IPTIME("ip", "IPTIME");

	private String code;
	private String name;

	ServiceType(String code, String type) {
		this.code = code;
		this.name = type;
	}

	@JsonCreator
	public static ServiceType codeOf(String code) {
		for (ServiceType type : ServiceType.values()) {
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid ServiceType code: " + code);
	}
}
