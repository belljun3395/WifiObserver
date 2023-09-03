package com.wifi.obs.app.exception.domain;

public class ServiceNotFoundException extends DomainException {

	private final Long serviceId;

	public ServiceNotFoundException(Long serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public String getMessage() {
		return "존재하지 않는 서비스입니다. serviceId: " + serviceId;
	}
}
