package com.observer.data.entity.router;

import lombok.Getter;

@Getter
public enum RouterServiceType {
	IPTIME("IPTIME");

	private final String type;

	RouterServiceType(String type) {
		this.type = type;
	}
}
