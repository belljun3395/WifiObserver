package com.wifi.observer.client.wifi.client.iptime;

import static com.wifi.observer.client.wifi.support.log.LogFormat.RESPONSE_ERROR;
import static java.lang.String.format;

import com.wifi.obs.infra.slack.config.SlackChannel;
import com.wifi.obs.infra.slack.service.SlackService;
import com.wifi.observer.client.wifi.dto.http.IptimeWifiHealthClientDto;
import com.wifi.observer.client.wifi.dto.request.WifiBulkHealthRequest;
import com.wifi.observer.client.wifi.dto.request.common.CommonWifiHealthRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.http.request.get.HealthClientQuery;
import com.wifi.observer.client.wifi.model.HealthQueryClientModel;
import com.wifi.observer.client.wifi.support.converter.iptime.IptimeWifiHealthConverter;
import com.wifi.observer.client.wifi.support.log.WifiClientTrace;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeHealthClientImpl implements IptimeHealthClient {

	private final IptimeWifiHealthConverter iptimeWifiHealthClientConverter;
	private final HealthClientQuery healthClientQuery;

	private final SlackService slackService;

	@Override
	@WifiClientTrace
	public ClientResponse<HttpStatus> query(CommonWifiHealthRequest request) {

		IptimeWifiHealthClientDto queryDto = getDto(request);

		HealthQueryClientModel response = healthClientQuery.query(queryDto);

		if (response.isFail()) {
			writeFailLog(response);
		}

		return iptimeWifiHealthClientConverter.from(response.getHttpStatus(), response.getHost());
	}

	@Override
	public List<ClientResponse<HttpStatus>> queries(WifiBulkHealthRequest requests) {
		return requests.getSource().stream().map(this::query).collect(Collectors.toList());
	}

	private IptimeWifiHealthClientDto getDto(CommonWifiHealthRequest request) {
		return iptimeWifiHealthClientConverter.to(request.getHost());
	}

	private void writeFailLog(HealthQueryClientModel response) {
		log.warn(RESPONSE_ERROR.getLogDebugFormat(), this.getClass().getName(), response.getHost());
		slackService.sendSlackMessage(
				format(RESPONSE_ERROR.getSlackFormat(), this.getClass().getName(), response.getHost()),
				SlackChannel.ERROR);
	}
}
