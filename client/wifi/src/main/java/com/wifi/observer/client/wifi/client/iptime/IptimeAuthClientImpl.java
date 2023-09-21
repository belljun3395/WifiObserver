package com.wifi.observer.client.wifi.client.iptime;

import static com.wifi.observer.client.wifi.support.log.LogFormat.RESPONSE_ERROR;
import static java.lang.String.format;

import com.wifi.obs.infra.slack.config.SlackChannel;
import com.wifi.obs.infra.slack.service.SlackService;
import com.wifi.observer.client.wifi.dto.http.IptimeWifiAuthClientDto;
import com.wifi.observer.client.wifi.dto.request.WifiAuthClientRequest;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeAuthRequest;
import com.wifi.observer.client.wifi.dto.response.AuthInfo;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.dto.response.iptime.IptimeAuthResponse;
import com.wifi.observer.client.wifi.http.request.post.AuthClientCommand;
import com.wifi.observer.client.wifi.model.AuthCommandClientModel;
import com.wifi.observer.client.wifi.support.converter.iptime.IptimeAuthConverter;
import com.wifi.observer.client.wifi.support.generator.iptime.IptimeAuthClientBodyGenerator;
import com.wifi.observer.client.wifi.support.generator.iptime.IptimeAuthClientHeaderGenerator;
import com.wifi.observer.client.wifi.util.resolver.CookieResolver;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeAuthClientImpl implements IptimeAuthClient {

	private final IptimeAuthConverter iptimeAuthConverter;
	private final IptimeAuthClientHeaderGenerator iptimeAuthClientHeaderGenerator;
	private final IptimeAuthClientBodyGenerator iptimeAuthClientBodyGenerator;
	private final CookieResolver cookieResolver;
	private final AuthClientCommand authClientCommand;

	private final SlackService slackService;

	@Override
	public ClientResponse<AuthInfo> command(IptimeAuthRequest request) {

		IptimeWifiAuthClientDto command = getDto(request);

		AuthCommandClientModel response = authClientCommand.command(command);

		if (response.isFail()) {
			writeFailLog(response);
			return IptimeAuthResponse.fail(response.getHost());
		}

		String cookie = cookieResolver.resolve(response.getAuthInfo());

		return iptimeAuthConverter.from(cookie, response.getHost());
	}

	private IptimeWifiAuthClientDto getDto(WifiAuthClientRequest request) {
		Map<String, String> headers = iptimeAuthClientHeaderGenerator.execute(request.getHost());
		Map<String, String> body =
				iptimeAuthClientBodyGenerator.execute(request.getUserName(), request.getPassword());

		return iptimeAuthConverter.to(request, headers, body);
	}

	private void writeFailLog(AuthCommandClientModel response) {
		log.warn(RESPONSE_ERROR.getLogDebugFormat(), this.getClass().getName(), response.getHost());
		slackService.sendSlackMessage(
				format(RESPONSE_ERROR.getSlackFormat(), this.getClass().getName(), response.getHost()),
				SlackChannel.ERROR);
	}
}
