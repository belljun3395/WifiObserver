package com.observer.client.router.service.iptime;

import com.observer.client.router.exception.ClientAuthException;
import com.observer.client.router.exception.ClientException;
import com.observer.client.router.http.client.iptime.IptimeAuthClient;
import com.observer.client.router.http.dto.generator.iptime.IptimeAuthClientBodyGenerator;
import com.observer.client.router.http.dto.generator.iptime.IptimeAuthClientHeaderGenerator;
import com.observer.client.router.http.dto.http.RouterConnectBody;
import com.observer.client.router.http.dto.http.iptime.IptimeWifiAuthClientDto;
import com.observer.client.router.service.RouterAuthService;
import com.observer.client.router.service.util.CookieResolver;
import com.observer.client.router.support.dto.request.iptime.IptimeAuthServiceRequest;
import com.observer.client.router.support.dto.response.RouterAuthInfo;
import com.observer.client.router.support.dto.response.RouterAuthResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeAuthService implements RouterAuthService<IptimeAuthServiceRequest> {

	private static final String HTTP = "http://";

	@Value("${iptime.http.login.handler}")
	private String loginHandler;

	private final IptimeAuthClientHeaderGenerator iptimeAuthClientHeaderGenerator;
	private final IptimeAuthClientBodyGenerator iptimeAuthClientBodyGenerator;
	private final CookieResolver cookieResolver;
	private final IptimeAuthClient authClientCommand;

	@Override
	public RouterAuthResponse execute(IptimeAuthServiceRequest request) {
		final String host = request.getHost();
		IptimeWifiAuthClientDto dto = getClientDto(request);

		RouterConnectBody<Document> auth = getAuth(dto);

		String cookie = cookieResolver.resolve(auth);
		if (cookie.isEmpty()) {
			throw new ClientAuthException();
		}

		return RouterAuthResponse.builder()
				.host(host)
				.response(RouterAuthInfo.builder().auth(cookie).build())
				.build();
	}

	private IptimeWifiAuthClientDto getClientDto(IptimeAuthServiceRequest request) {
		Map<String, String> headers = iptimeAuthClientHeaderGenerator.execute(request.getHost());
		Map<String, String> body =
				iptimeAuthClientBodyGenerator.execute(request.getUserName(), request.getPassword());
		return IptimeWifiAuthClientDto.builder()
				.url(HTTP + request.getHost() + loginHandler)
				.headers(headers)
				.body(body)
				.build();
	}

	private RouterConnectBody<Document> getAuth(IptimeWifiAuthClientDto dto) {
		RouterConnectBody<Document> clientResponse = null;
		try {
			clientResponse = authClientCommand.execute(dto);
		} catch (IOException e) {
			throw new ClientException(e);
		}
		return Objects.requireNonNull(clientResponse);
	}
}
