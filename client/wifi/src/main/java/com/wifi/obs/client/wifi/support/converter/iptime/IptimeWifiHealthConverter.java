package com.wifi.obs.client.wifi.support.converter.iptime;

import com.wifi.obs.client.wifi.dto.http.iptime.IptimeWifiHealthRequestElement;
import com.wifi.obs.client.wifi.dto.response.HealthStatusResponse;
import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import org.springframework.stereotype.Component;

@Component
public class IptimeWifiHealthConverter {

	private static final String HTTP = "http://";

	public HealthStatusResponse from(HttpStatusResponse status, String host) {
		return HealthStatusResponse.builder().response(status).host(host).build();
	}

	public IptimeWifiHealthRequestElement to(String host) {
		return IptimeWifiHealthRequestElement.builder().url(HTTP + host).build();
	}
}
