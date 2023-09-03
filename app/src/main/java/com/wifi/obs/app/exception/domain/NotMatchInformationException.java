package com.wifi.obs.app.exception.domain;

public class NotMatchInformationException extends DomainException {

	@Override
	public String getMessage() {
		return "해당 회원의 정보가 아닙니다.";
	}
}
