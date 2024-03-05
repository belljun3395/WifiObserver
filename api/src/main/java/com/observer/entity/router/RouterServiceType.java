package com.observer.entity.router;

import lombok.Getter;

@Getter
public enum RouterServiceType {
	IPTIME("IPTIME");

	private final String type;

	RouterServiceType(String type) {
		this.type = type;
	}
}
