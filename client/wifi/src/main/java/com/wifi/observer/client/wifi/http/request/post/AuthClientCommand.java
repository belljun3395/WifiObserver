package com.wifi.observer.client.wifi.http.request.post;

import com.wifi.observer.client.wifi.dto.http.IptimeWifiAuthClientDto;
import com.wifi.observer.client.wifi.support.log.WifiClientTrace;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Optional;
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
public class AuthClientCommand {

	@Value("${iptime.http.useragent}")
	private String useragent;

	@Value("${jsoup.auth.timeout}")
	private int timeout;

	@WifiClientTrace
	public Optional<Document> command(IptimeWifiAuthClientDto source) {
		try {
			return Optional.of(
					Jsoup.connect(source.getUrl())
							.userAgent(useragent)
							.headers(source.getHeaders())
							.data(source.getBody())
							.timeout(timeout)
							.post());
		} catch (SocketTimeoutException timeoutException) {
			return Optional.empty();
		} catch (IOException e) {
			return Optional.empty();
		}
	}
}
