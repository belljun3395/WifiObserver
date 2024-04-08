package com.observer.client.router.config;

import com.observer.client.router.http.client.iptime.IptimeUsersClient;
import com.observer.client.router.http.dto.http.iptime.IptimeRouterConnectBody;
import com.observer.client.router.http.dto.http.iptime.IptimeWifiBrowseClientDto;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.core.io.ClassPathResource;

@TestComponent
public class TestIptimeUsersClient implements IptimeUsersClient {

	private static final String ON_CONNECT_USER_HTML_PATH = "/html/iptime/iptimeOnConnect.html";

	@Override
	public IptimeRouterConnectBody execute(IptimeWifiBrowseClientDto source) throws IOException {
		ClassPathResource authResource = new ClassPathResource(ON_CONNECT_USER_HTML_PATH);
		Document document = Jsoup.parse(authResource.getFile());

		return IptimeRouterConnectBody.builder().body(document).build();
	}
}
