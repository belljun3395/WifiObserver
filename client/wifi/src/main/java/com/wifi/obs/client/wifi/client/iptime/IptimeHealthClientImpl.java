package com.wifi.obs.client.wifi.client.iptime;

import static com.wifi.obs.client.wifi.support.log.LogFormat.RESPONSE_ERROR;
import static java.lang.String.format;

import com.wifi.obs.client.wifi.dto.http.WifiHealthRequestElement;
import com.wifi.obs.client.wifi.dto.request.WifiHostRequest;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import com.wifi.obs.client.wifi.http.request.get.HealthClientQuery;
import com.wifi.obs.client.wifi.model.Health;
import com.wifi.obs.client.wifi.support.converter.iptime.IptimeWifiHealthConverter;
import com.wifi.obs.infra.slack.service.ErrorNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeHealthClientImpl extends IptimeHealthClient {

	private final IptimeWifiHealthConverter iptimeWifiHealthClientConverter;
	private final HealthClientQuery healthClientQuery;

	private final ErrorNotificationService errorNotificationService;

	@Override
	protected WifiHealthRequestElement getRequestElement(WifiHostRequest request) {
		return iptimeWifiHealthClientConverter.to(request.getHost());
	}

	@Override
	public Health executeQuery(WifiHealthRequestElement data) {
		return healthClientQuery.query(data);
	}

	@Override
	protected ClientResponse<HttpStatusResponse> getClientResponse(Health response) {
		return iptimeWifiHealthClientConverter.from(response.getHttpStatus(), response.getHost());
	}

	@Override
	protected void writeFailLog(Health response) {
		String className = this.getClass().getName();
		String host = response.getHost();
		log.warn(RESPONSE_ERROR.getFormat(), className, host);
		errorNotificationService.sendNotification(
				format(RESPONSE_ERROR.getSlackFormat(), className, host));
	}
}
