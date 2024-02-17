package com.observer.client.router.http.dto.generator.iptime;

import com.observer.client.router.config.properties.IptimeRouterHttpProperties;
import com.observer.client.router.http.dto.generator.CommonHeaderGenerator;
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

	private final IptimeRouterHttpProperties httpProperties;
	private final CommonHeaderGenerator commonHeaderGenerator;

	public Map<String, String> execute(String host) {
		Map<String, String> headers = commonHeaderGenerator.getIptimeCommonHeader(host);
		headers.put(REFERER, HTTP + host + httpProperties.getLoginSession());
		headers.put(CONTENT_LENGTH, httpProperties.getContentLength());
		headers.put(CONTENT_TYPE, httpProperties.getContentType());
		return headers;
	}
}
