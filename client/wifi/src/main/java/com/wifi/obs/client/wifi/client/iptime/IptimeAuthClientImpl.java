package com.wifi.obs.client.wifi.client.iptime;

import static com.wifi.obs.client.wifi.support.log.LogFormat.RESPONSE_ERROR;
import static java.lang.String.format;

import com.wifi.obs.client.wifi.dto.http.WifiAuthRequestElement;
import com.wifi.obs.client.wifi.dto.request.iptime.IptimeAuthRequest;
import com.wifi.obs.client.wifi.dto.response.AuthInfo;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.http.HTMLResponse;
import com.wifi.obs.client.wifi.http.request.post.AuthClientCommand;
import com.wifi.obs.client.wifi.model.Auth;
import com.wifi.obs.client.wifi.support.converter.iptime.IptimeAuthConverter;
import com.wifi.obs.client.wifi.support.generator.iptime.IptimeAuthClientBodyGenerator;
import com.wifi.obs.client.wifi.support.generator.iptime.IptimeAuthClientHeaderGenerator;
import com.wifi.obs.client.wifi.util.resolver.CookieResolver;
import com.wifi.obs.infra.slack.service.ErrorNotificationService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeAuthClientImpl extends IptimeAuthClient {

	private final IptimeAuthConverter iptimeAuthConverter;
	private final IptimeAuthClientHeaderGenerator iptimeAuthClientHeaderGenerator;
	private final IptimeAuthClientBodyGenerator iptimeAuthClientBodyGenerator;
	private final CookieResolver cookieResolver;
	private final AuthClientCommand authClientCommand;

	private final ErrorNotificationService errorNotificationService;

	@Override
	protected WifiAuthRequestElement getRequestElement(IptimeAuthRequest request) {
		Map<String, String> headers = iptimeAuthClientHeaderGenerator.execute(request.getHost());
		Map<String, String> body =
				iptimeAuthClientBodyGenerator.execute(request.getUserName(), request.getPassword());

		return iptimeAuthConverter.to(request, headers, body);
	}

	@Override
	protected String executeCommand(WifiAuthRequestElement data) {
		HTMLResponse response = authClientCommand.command(data);
		if (response.isFail()) {
			return Strings.EMPTY;
		}
		return cookieResolver.resolve(response);
	}

	@Override
	protected ClientResponse<AuthInfo> getClientResponse(Auth source) {
		return iptimeAuthConverter.from(source.getAuthInfo(), source.getHost());
	}

	@Override
	protected void writeFailLog(String host) {
		String className = this.getClass().getName();
		log.warn(RESPONSE_ERROR.getFormat(), className, host);
		errorNotificationService.sendNotification(
				format(RESPONSE_ERROR.getSlackFormat(), className, host));
	}
}
