package com.wifi.observer.client.wifi.support.converter.iptime;

import com.wifi.observer.client.wifi.dto.http.IptimeWifiHealthClientDto;
import com.wifi.observer.client.wifi.dto.response.common.CommonHealthStatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class IptimeWifiHealthConverter {

	private static final String HTTP = "http://";

	public CommonHealthStatusResponse from(HttpStatus status, String host) {
		return CommonHealthStatusResponse.builder().response(status).host(host).build();
	}

	public IptimeWifiHealthClientDto to(String host) {
		return IptimeWifiHealthClientDto.builder().url(HTTP + host).build();
	}
}
