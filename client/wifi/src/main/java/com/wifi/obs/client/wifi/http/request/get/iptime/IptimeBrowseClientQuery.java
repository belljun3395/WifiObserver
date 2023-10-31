package com.wifi.obs.client.wifi.http.request.get.iptime;

import com.wifi.obs.client.wifi.dto.http.WifiBrowseRequestElement;
import com.wifi.obs.client.wifi.http.HTMLResponse;
import com.wifi.obs.client.wifi.http.jsoup.ClientDocument;
import com.wifi.obs.client.wifi.http.jsoup.ClientJsoup;
import com.wifi.obs.client.wifi.http.request.get.BrowseClientQuery;
import com.wifi.obs.client.wifi.support.log.WifiClientTrace;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IptimeBrowseClientQuery implements BrowseClientQuery {

	private static final String COOKIE_NAME = "efm_session_id";

	@Value("${iptime.http.useragent}")
	private String useragent;

	@Value("${jsoup.browse.timeout}")
	private int timeout;

	@WifiClientTrace
	public HTMLResponse query(WifiBrowseRequestElement source) {
		try {
			return HTMLResponse.of(execute(source));
		} catch (IOException e) {
			return HTMLResponse.of(ClientJsoup.getFailDocument());
		}
	}

	private ClientDocument execute(WifiBrowseRequestElement source) throws IOException {
		return ClientJsoup.connect(source.getUrl())
				.userAgent(useragent)
				.cookie(COOKIE_NAME, source.getCookie())
				.headers(source.getHeaders())
				.timeout(timeout)
				.get();
	}
}
