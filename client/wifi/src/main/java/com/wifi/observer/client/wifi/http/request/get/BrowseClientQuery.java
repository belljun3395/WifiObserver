package com.wifi.observer.client.wifi.http.request.get;

import com.wifi.observer.client.wifi.dto.http.IptimeWifiBrowseClientDto;
import com.wifi.observer.client.wifi.support.log.WifiClientTrace;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BrowseClientQuery {

	private static final String COOKIE_NAME = "efm_session_id";

	@Value("${iptime.http.useragent}")
	private String useragent;

	@Value("${jsoup.browse.timeout}")
	private int timeout;

	@WifiClientTrace
	public Optional<Document> query(IptimeWifiBrowseClientDto source) {
		try {
			return Optional.of(
					Jsoup.connect(source.getUrl())
							.userAgent(useragent)
							.cookie(COOKIE_NAME, source.getCookie())
							.headers(source.getHeaders())
							.timeout(timeout)
							.get());
		} catch (SocketTimeoutException timeoutException) {
			return Optional.empty();
		} catch (IOException e) {
			return Optional.empty();
		}
	}
}
