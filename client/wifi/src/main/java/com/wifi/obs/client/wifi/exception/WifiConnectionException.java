package com.wifi.obs.client.wifi.exception;

import lombok.Getter;

/** 와이파이 연결에 실패했을 때 발생하는 예외 */
@Getter
public class WifiConnectionException extends WifiRuntimeException {

	public WifiConnectionException(String host) {
		super(host);
	}

	@Override
	public String getMessage() {
		return "와이파이 연결에 실패했습니다. host : " + super.getHost();
	}
}
