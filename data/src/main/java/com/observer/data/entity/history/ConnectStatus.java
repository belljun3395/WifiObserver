package com.observer.data.entity.history;

import lombok.Getter;

@Getter
public enum ConnectStatus {
	CONNECTED("CONNECTED"),
	DISCONNECTED("DISCONNECTED");

	private String status;

	ConnectStatus(String status) {
		this.status = status;
	}
}
