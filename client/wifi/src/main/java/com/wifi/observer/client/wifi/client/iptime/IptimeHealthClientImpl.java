package com.wifi.observer.client.wifi.client.iptime;

import com.wifi.obs.infra.slack.config.SlackChannel;
import com.wifi.obs.infra.slack.service.SlackService;
import com.wifi.observer.client.wifi.client.WifiHealthClient;
import com.wifi.observer.client.wifi.dto.http.IptimeWifiHealthClientDto;
import com.wifi.observer.client.wifi.dto.request.WifiBulkHealthRequest;
import com.wifi.observer.client.wifi.dto.request.common.CommonWifiHealthRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.dto.response.common.CommonHealthStatusResponse;
import com.wifi.observer.client.wifi.http.request.get.HealthClientQuery;
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
public class IptimeHealthClientImpl implements WifiHealthClient {

	private final IptimeWifiHealthConverter iptimeWifiHealthClientConverter;
	private final HealthClientQuery healthClientQuery;

	private final SlackService slackService;

	@Override
	@WifiClientTrace
	public CommonHealthStatusResponse query(CommonWifiHealthRequest request) {
		IptimeWifiHealthClientDto queryDto = getDto(request);

		HttpStatus status = healthClientQuery.query(queryDto);

		if (status != HttpStatus.OK) {
			slackService.sendSlackMessage(
					"iptime health client response is empty!!! host : " + request.getHost(),
					SlackChannel.ERROR);
		}

		return iptimeWifiHealthClientConverter.from(status, request.getHost());
	}

	@Override
	public List<ClientResponse<HttpStatus>> queries(WifiBulkHealthRequest requests) {
		return requests.getSource().stream().map(this::query).collect(Collectors.toList());
	}

	private IptimeWifiHealthClientDto getDto(CommonWifiHealthRequest request) {
		return iptimeWifiHealthClientConverter.to(request.getHost());
	}
}
