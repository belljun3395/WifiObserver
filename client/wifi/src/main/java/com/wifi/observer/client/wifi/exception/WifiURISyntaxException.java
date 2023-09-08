package com.wifi.observer.client.wifi.exception;

import lombok.Getter;

@Getter
public class WifiURISyntaxException extends WifiRuntimeException {

	public WifiURISyntaxException(String host) {
		super(host);
	}

	@Override
	public String getMessage() {
		return "올바르지 않은 URI 형식입니다. host : " + super.getHost();
	}
}
