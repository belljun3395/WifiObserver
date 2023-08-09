package com.wifi.observer.client.wifi.exception;

import lombok.Getter;

/** 와이파이 런타임과 관련된 공통 예외 */
@Getter
public abstract class WifiRuntimeException extends RuntimeException {
	private final String host;

	protected WifiRuntimeException(String host) {
		this.host = host;
	}

	@Override
	public abstract String getMessage();
}
