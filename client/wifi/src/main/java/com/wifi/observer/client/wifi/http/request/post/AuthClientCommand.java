package com.wifi.observer.client.wifi.http.request.post;

import com.wifi.observer.client.wifi.dto.http.IptimeWifiAuthClientDto;
import com.wifi.observer.client.wifi.model.AuthCommandClientModel;
import com.wifi.observer.client.wifi.model.info.AuthCommandInfo;
import com.wifi.observer.client.wifi.support.jsoup.ClientJsoup;
import com.wifi.observer.client.wifi.support.log.WifiClientTrace;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	public AuthCommandClientModel command(IptimeWifiAuthClientDto source) {
		try {
			return AuthCommandClientModel.builder()
					.authInfo(
							AuthCommandInfo.builder()
									.info(
											ClientJsoup.connect(source.getUrl())
													.userAgent(useragent)
													.headers(source.getHeaders())
													.data(source.getBody())
													.timeout(timeout)
													.post())
									.build())
					.host(source.getHost())
					.build();
		} catch (IOException e) {
			return AuthCommandClientModel.fail(source.getHost());
		}
	}
}
