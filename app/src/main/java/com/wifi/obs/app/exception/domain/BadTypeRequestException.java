package com.wifi.obs.app.exception.domain;

public class BadTypeRequestException extends DomainException {

	@Override
	public String getMessage() {
		return "잘못된 타입의 요청입니다.";
	}
}
