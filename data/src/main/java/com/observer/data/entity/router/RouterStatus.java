package com.observer.data.entity.router;

import lombok.Getter;

@Getter
public enum RouterStatus {
	ON("ON"),
	ERROR("ERROR");

	private String type;

	RouterStatus(String type) {
		this.type = type;
	}
}
