package com.wifi.obs.app.exception.domain;

public class MemberNotFoundException extends DomainException {
	private final Long memberId;

	public MemberNotFoundException(Long memberId) {
		this.memberId = memberId;
	}

	@Override
	public String getMessage() {
		return "존재하지 않는 회원입니다. memberId: " + memberId;
	}
}
