package com.observer.client.router.http.client.iptime;

import com.observer.client.router.http.dto.http.iptime.IptimeRouterConnectBody;
import com.observer.client.router.http.dto.http.iptime.IptimeWifiBrowseClientDto;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("!client-test")
@Component
public class IptimeUsersClientImpl implements IptimeUsersClient {

	private static final String COOKIE_NAME = "efm_session_id";

	@Value("${iptime.http.useragent}")
	private String useragent;

	@Value("${jsoup.browse.timeout}")
	private int timeout;

	@Override
	public IptimeRouterConnectBody execute(IptimeWifiBrowseClientDto source) throws IOException {
		Document document =
				Jsoup.connect(source.getUrl())
						.headers(source.getHeaders())
						.cookie(COOKIE_NAME, source.getCookie())
						.userAgent(useragent)
						.timeout(timeout)
						.get();
		return IptimeRouterConnectBody.builder().body(document).build();
	}
}
