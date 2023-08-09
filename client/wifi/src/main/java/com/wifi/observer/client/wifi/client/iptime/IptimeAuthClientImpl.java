package com.wifi.observer.client.wifi.client.iptime;

import com.wifi.observer.client.wifi.client.WifiAuthClient;
import com.wifi.observer.client.wifi.dto.http.IptimeWifiAuthClientDto;
import com.wifi.observer.client.wifi.dto.request.WifiAuthClientRequest;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeAuthRequest;
import com.wifi.observer.client.wifi.dto.response.AuthInfo;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.dto.response.iptime.IptimeAuthResponse;
import com.wifi.observer.client.wifi.http.request.post.AuthClientCommand;
import com.wifi.observer.client.wifi.support.converter.iptime.IptimeAuthConverter;
import com.wifi.observer.client.wifi.support.generator.iptime.IptimeAuthClientBodyGenerator;
import com.wifi.observer.client.wifi.support.generator.iptime.IptimeAuthClientHeaderGenerator;
import com.wifi.observer.client.wifi.util.resolver.CookieResolver;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeAuthClientImpl implements WifiAuthClient<IptimeAuthRequest, AuthInfo> {

	private final IptimeAuthConverter iptimeAuthConverter;
	private final IptimeAuthClientHeaderGenerator iptimeAuthClientHeaderGenerator;
	private final IptimeAuthClientBodyGenerator iptimeAuthClientBodyGenerator;
	private final CookieResolver cookieResolver;
	private final AuthClientCommand authClientCommand;

	@Override
	public ClientResponse<AuthInfo> command(IptimeAuthRequest request) {

		IptimeWifiAuthClientDto command = getDto(request);

		Optional<Document> response = authClientCommand.command(command);

		if (response.isEmpty()) {
			return IptimeAuthResponse.fail(request.getHost());
		}

		return iptimeAuthConverter.from(cookieResolver.resolve(response.get()), request.getHost());
	}

	private IptimeWifiAuthClientDto getDto(WifiAuthClientRequest request) {

		Map<String, String> headers = iptimeAuthClientHeaderGenerator.execute(request.getHost());
		Map<String, String> body =
				iptimeAuthClientBodyGenerator.execute(request.getUserName(), request.getPassword());

		return iptimeAuthConverter.to(request, headers, body);
	}
}
