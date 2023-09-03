package com.wifi.obs.app.exception.domain;

public class AlreadyRegisterException extends DomainException {

	@Override
	public String getMessage() {
		return "이미 등록되었습니다.";
	}
}
