package com.observer.client.router.http.dto.generator.iptime;

import com.observer.client.router.config.properties.IptimeRouterHttpProperties;
import com.observer.client.router.http.dto.generator.CommonHeaderGenerator;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IptimeBrowseClientHeaderGenerator {

	private static final String HTTP = "http://";
	private static final String REFERER = "Referer";

	private final IptimeRouterHttpProperties httpProperties;
	private final CommonHeaderGenerator commonHeaderGenerator;

	public Map<String, String> execute(String host) {
		Map<String, String> headers = commonHeaderGenerator.getIptimeCommonHeader(host);
		headers.put(REFERER, HTTP + host + httpProperties.getLoginHandler());
		return headers;
	}
}
