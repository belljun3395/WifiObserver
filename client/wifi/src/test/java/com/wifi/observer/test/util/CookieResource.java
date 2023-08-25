package com.wifi.observer.test.util;

import com.wifi.observer.client.wifi.client.iptime.IptimeAuthClientImpl;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeAuthRequest;
import com.wifi.observer.client.wifi.dto.response.AuthInfo;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.exception.WifiConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@TestComponent
@TestPropertySource("classpath:application-test.yml")
public class CookieResource {
	@Value("${test.host}")
	String host;

	@Value("${test.userName}")
	String userName;

	@Value("${test.password}")
	String password;

	@Autowired IptimeAuthClientImpl iptimeAuthClient;

	String cookie;
	String DEFAULT_COOKE_VALUE = "2Lr3BFTO4DCFeGQF";

	public String getCookie() {
		if (cookie == null) {
			ClientResponse<AuthInfo> cookieResponse =
					iptimeAuthClient.command(
							IptimeAuthRequest.builder().host(host).userName(userName).password(password).build());
			if (cookieResponse.getResponse().isPresent()) {
				cookie = cookieResponse.getResponse().get().getInfo();
				log.debug("쿠키 값 획득 : {}", cookie);
			} else {
				throw new WifiConnectionException("쿠키 값 획득 실패");
			}
			return cookie;
		}
		return cookie;
	}
}
