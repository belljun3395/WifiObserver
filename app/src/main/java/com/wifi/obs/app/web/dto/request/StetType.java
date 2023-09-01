package com.wifi.obs.app.web.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum StetType {
	MONTH("m"),
	DAY("d");

	private String code;

	StetType(String code) {
		this.code = code;
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
