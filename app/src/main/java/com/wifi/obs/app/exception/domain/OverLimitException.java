package com.wifi.obs.app.exception.domain;

public class OverLimitException extends DomainException {

	@Override
	public String getMessage() {
		return "더 이상 등록할 수 없습니다.";
	}
}
