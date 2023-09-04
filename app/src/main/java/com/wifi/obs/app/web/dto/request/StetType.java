package com.wifi.obs.app.web.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum StetType {
	MONTH("m", "Month"),
	DAY("d", "Day");

	private final String code;
	private final String type;

	StetType(String code, String type) {
		this.code = code;
		this.type = type;
	}

	@JsonCreator
	public static StetType codeOf(String code) {
		for (StetType type : StetType.values()) {
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid StetType code: " + code);
	}
}
