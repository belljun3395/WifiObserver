package com.wifi.obs.app.exception.domain;

public class NotMatchPasswordException extends DomainException {
	@Override
	public String getMessage() {
		return "비밀번호가 일치하지 않습니다.";
	}
}
