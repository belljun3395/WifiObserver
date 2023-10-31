package com.wifi.obs.client.wifi.http.request.post.iptime;

import com.wifi.obs.client.wifi.dto.http.WifiAuthRequestElement;
import com.wifi.obs.client.wifi.http.HTMLResponse;
import com.wifi.obs.client.wifi.http.jsoup.ClientDocument;
import com.wifi.obs.client.wifi.http.jsoup.ClientJsoup;
import com.wifi.obs.client.wifi.http.request.post.AuthClientCommand;
import com.wifi.obs.client.wifi.support.log.WifiClientTrace;
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
public class IptimeAuthClientCommand implements AuthClientCommand {

	@Value("${iptime.http.useragent}")
	private String useragent;

	@Value("${jsoup.auth.timeout}")
	private int timeout;

	@WifiClientTrace
	public HTMLResponse command(WifiAuthRequestElement source) {
		try {
			return HTMLResponse.of(executeCommand(source));
		} catch (IOException e) {
			return HTMLResponse.of(ClientJsoup.getFailDocument());
		}
	}

	private ClientDocument executeCommand(WifiAuthRequestElement source) throws IOException {
		return ClientJsoup.connect(source.getUrl())
				.userAgent(useragent)
				.headers(source.getHeaders())
				.data(source.getBody())
				.timeout(timeout)
				.post();
	}
}
