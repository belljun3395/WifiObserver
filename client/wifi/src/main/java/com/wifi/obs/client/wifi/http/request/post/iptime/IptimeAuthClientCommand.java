package com.wifi.obs.client.wifi.http.request.post.iptime;

import com.wifi.obs.client.wifi.dto.http.WifiAuthRequestElement;
import com.wifi.obs.client.wifi.http.jsoup.ClientJsoup;
import com.wifi.obs.client.wifi.http.jsoup.HTMLDocumentResponse;
import com.wifi.obs.client.wifi.http.request.post.AuthClientCommand;
import com.wifi.obs.client.wifi.model.Auth;
import com.wifi.obs.client.wifi.model.value.AuthCommandVO;
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
	public Auth command(WifiAuthRequestElement source) {
		try {
			return Auth.builder().authInfo(getAuthInfo(source)).host(source.getHost()).build();
		} catch (IOException e) {
			return Auth.fail(source.getHost());
		}
	}

	private AuthCommandVO getAuthInfo(WifiAuthRequestElement source) throws IOException {
		return AuthCommandVO.builder().info(executeCommand(source)).build();
	}

	private HTMLDocumentResponse executeCommand(WifiAuthRequestElement source) throws IOException {
		return ClientJsoup.connect(source.getUrl())
				.userAgent(useragent)
				.headers(source.getHeaders())
				.data(source.getBody())
				.timeout(timeout)
				.post();
	}
}
