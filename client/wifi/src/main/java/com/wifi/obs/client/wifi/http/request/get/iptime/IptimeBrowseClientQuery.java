package com.wifi.obs.client.wifi.http.request.get.iptime;

import com.wifi.obs.client.wifi.dto.http.WifiBrowseRequestElement;
import com.wifi.obs.client.wifi.http.jsoup.ClientJsoup;
import com.wifi.obs.client.wifi.http.jsoup.HTMLDocumentResponse;
import com.wifi.obs.client.wifi.http.request.get.BrowseClientQuery;
import com.wifi.obs.client.wifi.model.Users;
import com.wifi.obs.client.wifi.model.value.BrowseQueryVO;
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
	public Users query(WifiBrowseRequestElement source) {
		try {
			return Users.builder().usersInfo(getBrowseQueryInfo(source)).host(source.getHost()).build();
		} catch (IOException e) {
			return Users.fail(source.getHost());
		}
	}

	private BrowseQueryVO getBrowseQueryInfo(WifiBrowseRequestElement source) throws IOException {
		return BrowseQueryVO.builder().info(executeQuery(source)).build();
	}

	private HTMLDocumentResponse executeQuery(WifiBrowseRequestElement source) throws IOException {
		return ClientJsoup.connect(source.getUrl())
				.userAgent(useragent)
				.cookie(COOKIE_NAME, source.getCookie())
				.headers(source.getHeaders())
				.timeout(timeout)
				.get();
	}
}
