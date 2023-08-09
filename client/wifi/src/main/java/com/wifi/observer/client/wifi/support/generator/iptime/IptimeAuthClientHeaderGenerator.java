package com.wifi.observer.client.wifi.support.generator.iptime;

import com.wifi.observer.client.wifi.support.generator.CommonHeaderGenerator;
import com.wifi.observer.client.wifi.support.property.IptimeWifiHttpProperty;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IptimeAuthClientHeaderGenerator {
	private static final String HTTP = "http://";
	private static final String REFERER = "Referer";
	private static final String CONTENT_LENGTH = "Content-Length";
	private static final String CONTENT_TYPE = "Content-Type";

	private final IptimeWifiHttpProperty iptimeWifiHttpProperty;
	private final CommonHeaderGenerator commonHeaderGenerator;

	public Map<String, String> execute(String host) {
		Map<String, String> headers = commonHeaderGenerator.getIptimeCommonHeader(host);
		headers.put(REFERER, HTTP + host + iptimeWifiHttpProperty.getLoginSession());
		headers.put(CONTENT_LENGTH, iptimeWifiHttpProperty.getContentLength());
		headers.put(CONTENT_TYPE, iptimeWifiHttpProperty.getContentType());
		return headers;
	}
}
