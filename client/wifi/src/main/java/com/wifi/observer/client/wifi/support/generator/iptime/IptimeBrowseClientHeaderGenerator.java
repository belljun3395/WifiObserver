package com.wifi.observer.client.wifi.support.generator.iptime;

import com.wifi.observer.client.wifi.support.generator.CommonHeaderGenerator;
import com.wifi.observer.client.wifi.support.property.IptimeWifiHttpProperty;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IptimeBrowseClientHeaderGenerator {

	private static final String HTTP = "http://";
	private static final String REFERER = "Referer";

	private final IptimeWifiHttpProperty iptimeWifiHttpProperty;
	private final CommonHeaderGenerator commonHeaderGenerator;

	public Map<String, String> execute(String host) {
		Map<String, String> headers = commonHeaderGenerator.getIptimeCommonHeader(host);
		headers.put(REFERER, HTTP + host + iptimeWifiHttpProperty.getLoginHandler());
		return headers;
	}
}
