package com.wifi.obs.app.exception.domain;

public class IncludeNotAllowHostNumberException extends DomainException {

	public final String badNumber;

	public IncludeNotAllowHostNumberException(String badNumber) {
		this.badNumber = badNumber;
	}

	@Override
	public String getMessage() {
		return "잘못된 주소입니다. " + badNumber + " 는 포함될 수 없습니다.";
	}
}
