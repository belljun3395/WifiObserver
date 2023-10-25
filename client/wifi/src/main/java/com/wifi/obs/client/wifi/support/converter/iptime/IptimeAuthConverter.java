package com.wifi.obs.client.wifi.support.converter.iptime;

import com.wifi.obs.client.wifi.dto.http.iptime.IptimeWifiAuthRequestElement;
import com.wifi.obs.client.wifi.dto.request.WifiAuthRequest;
import com.wifi.obs.client.wifi.dto.response.AuthInfo;
import com.wifi.obs.client.wifi.dto.response.iptime.IptimeAuthResponse;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IptimeAuthConverter {

	private static final String HTTP = "http://";

	@Value("${iptime.http.login.handler}")
	private String loginHandler;

	public IptimeAuthResponse from(String info, String host) {
		AuthInfo authInfo = AuthInfo.builder().info(info).build();
		return IptimeAuthResponse.builder().host(host).response(authInfo).build();
	}

	public IptimeWifiAuthRequestElement to(
			WifiAuthRequest request, Map<String, String> headers, Map<String, String> body) {
		return IptimeWifiAuthRequestElement.builder()
				.url(HTTP + request.getHost() + loginHandler)
				.headers(headers)
				.body(body)
				.build();
	}
}
