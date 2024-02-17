package com.observer.client.router.http.dto.generator;

import com.observer.client.router.config.properties.IptimeRouterHttpProperties;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommonHeaderGenerator {

	private static final String HTTP = "http://";
	private static final String ACCEPT = "Accept";
	private static final String ACCEPT_ENCODING = "Accept-Encoding";
	private static final String ACCEPT_LANGUAGE = "Accept-Language";
	private static final String CACHE_CONTROL = "Cache-Control";
	private static final String CONNECTION = "Connection";
	private static final String HOST = "Host";
	private static final String ORIGIN = "Origin";
	private static final String UPGRADE_INSECURE_REQUEST = "Upgrade-Insecure-Request";

	private final IptimeRouterHttpProperties iptimeWifiHttpProperty;

	public Map<String, String> getIptimeCommonHeader(String host) {
		HashMap<String, String> headers = new HashMap<>();
		headers.put(ACCEPT, iptimeWifiHttpProperty.getAccept());
		headers.put(ACCEPT_ENCODING, iptimeWifiHttpProperty.getAcceptEncoding());
		headers.put(ACCEPT_LANGUAGE, iptimeWifiHttpProperty.getAcceptLanguage());
		headers.put(CACHE_CONTROL, iptimeWifiHttpProperty.getCacheControl());
		headers.put(CONNECTION, iptimeWifiHttpProperty.getConnection());
		headers.put(HOST, host);
		headers.put(ORIGIN, HTTP + host);
		headers.put(UPGRADE_INSECURE_REQUEST, iptimeWifiHttpProperty.getUpgradeInsecureRequest());
		return headers;
	}
}
