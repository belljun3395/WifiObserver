package com.observer.client.router.http.client.iptime;

import com.observer.client.router.http.client.AuthClient;
import com.observer.client.router.http.dto.http.iptime.IptimeRouterConnectBody;
import com.observer.client.router.http.dto.http.iptime.IptimeWifiAuthClientDto;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor
@AllArgsConstructor
public class IptimeAuthClient implements AuthClient<IptimeWifiAuthClientDto, Document> {

	@Value("${iptime.http.useragent}")
	private String useragent;

	@Value("${jsoup.auth.timeout}")
	private int timeout;

	@Override
	public IptimeRouterConnectBody execute(IptimeWifiAuthClientDto source) throws IOException {
		Document document =
				Jsoup.connect(source.getUrl())
						.headers(source.getHeaders())
						.data(source.getBody())
						.userAgent(useragent)
						.timeout(timeout)
						.post();
		return IptimeRouterConnectBody.builder().body(document).build();
	}
}
