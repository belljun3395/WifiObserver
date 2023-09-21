package com.wifi.observer.client.wifi.http.request.get;

import com.wifi.observer.client.wifi.dto.http.IptimeWifiBrowseClientDto;
import com.wifi.observer.client.wifi.model.BrowseQueryClientModel;
import com.wifi.observer.client.wifi.model.info.BrowseQueryInfo;
import com.wifi.observer.client.wifi.support.jsoup.ClientJsoup;
import com.wifi.observer.client.wifi.support.log.WifiClientTrace;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
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
	public BrowseQueryClientModel query(IptimeWifiBrowseClientDto source) {
		try {
			return BrowseQueryClientModel.builder()
					.usersInfo(
							BrowseQueryInfo.builder()
									.info(
											ClientJsoup.connect(source.getUrl())
													.userAgent(useragent)
													.cookie(COOKIE_NAME, source.getCookie())
													.headers(source.getHeaders())
													.timeout(timeout)
													.get())
									.build())
					.host(source.getHost())
					.build();
		} catch (IOException e) {
			return BrowseQueryClientModel.fail(source.getHost());
		}
	}
}
