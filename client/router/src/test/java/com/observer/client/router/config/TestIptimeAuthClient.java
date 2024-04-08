package com.observer.client.router.config;

import com.observer.client.router.http.client.iptime.IptimeAuthClient;
import com.observer.client.router.http.dto.http.iptime.IptimeRouterConnectBody;
import com.observer.client.router.http.dto.http.iptime.IptimeWifiAuthClientDto;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.core.io.ClassPathResource;

@TestComponent
public class TestIptimeAuthClient implements IptimeAuthClient {
	private static final String AUTH_HTML_PATH = "/html/iptime/iptimeAuth.html";

	@Override
	public IptimeRouterConnectBody execute(IptimeWifiAuthClientDto source) throws IOException {
		ClassPathResource authResource = new ClassPathResource(AUTH_HTML_PATH);
		Document document = Jsoup.parse(authResource.getFile());

		return IptimeRouterConnectBody.builder().body(document).build();
	}
}
