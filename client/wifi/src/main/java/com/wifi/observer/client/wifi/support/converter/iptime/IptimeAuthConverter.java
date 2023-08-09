package com.wifi.observer.client.wifi.support.converter.iptime;

import com.wifi.observer.client.wifi.dto.http.IptimeWifiAuthClientDto;
import com.wifi.observer.client.wifi.dto.request.WifiAuthClientRequest;
import com.wifi.observer.client.wifi.dto.response.AuthInfo;
import com.wifi.observer.client.wifi.dto.response.iptime.IptimeAuthResponse;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IptimeAuthConverter {

	private static final String HTTP = "http://";

	@Value("${iptime.http.login.handler}")
	private String loginHandler;

	public IptimeAuthResponse from(String info, String host) {
		AuthInfo authInfo = new AuthInfo(info, host);
		return IptimeAuthResponse.builder().host(host).response(authInfo).build();
	}

	public IptimeWifiAuthClientDto to(
			WifiAuthClientRequest request, Map<String, String> headers, Map<String, String> body) {
		return IptimeWifiAuthClientDto.builder()
				.url(HTTP + request.getHost() + loginHandler)
				.headers(headers)
				.body(body)
				.build();
	}
}
