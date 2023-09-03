package com.wifi.obs.app.exception.domain;

public class ClientProblemException extends DomainException {

	@Override
	public String getMessage() {
		return "현재 공유기 조회 서비스에 문제가 있습니다.";
	}
}
