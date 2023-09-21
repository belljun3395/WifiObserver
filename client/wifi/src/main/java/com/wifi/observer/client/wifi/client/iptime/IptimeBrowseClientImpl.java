package com.wifi.observer.client.wifi.client.iptime;

import static com.wifi.observer.client.wifi.support.log.LogFormat.RESPONSE_ERROR;
import static java.lang.String.format;

import com.wifi.obs.infra.slack.config.SlackChannel;
import com.wifi.obs.infra.slack.service.SlackService;
import com.wifi.observer.client.wifi.dto.http.IptimeWifiBrowseClientDto;
import com.wifi.observer.client.wifi.dto.request.WifiBrowseRequest;
import com.wifi.observer.client.wifi.dto.request.WifiBulkBrowseRequest;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBrowseRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.dto.response.OnConnectUserInfos;
import com.wifi.observer.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
import com.wifi.observer.client.wifi.exception.WifiRuntimeException;
import com.wifi.observer.client.wifi.http.request.get.BrowseClientQuery;
import com.wifi.observer.client.wifi.model.BrowseQueryClientModel;
import com.wifi.observer.client.wifi.support.converter.iptime.IptimeBrowseConverter;
import com.wifi.observer.client.wifi.support.generator.iptime.IptimeBrowseClientHeaderGenerator;
import com.wifi.observer.client.wifi.util.resolver.users.IptimeUsersOnConnectFilterDecorator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeBrowseClientImpl implements IptimeBrowseClient {

	private final IptimeBrowseConverter iptimeBrowseConverter;
	private final IptimeBrowseClientHeaderGenerator iptimeBrowseClientHeaderGenerator;
	private final IptimeUsersOnConnectFilterDecorator iptimeUsersOnConnectFilterDecorator;
	private final BrowseClientQuery browseClientQuery;

	private final SlackService slackService;

	@Override
	public ClientResponse<OnConnectUserInfos> query(IptimeBrowseRequest request)
			throws WifiRuntimeException {

		IptimeWifiBrowseClientDto query = getDto(request);

		BrowseQueryClientModel response = browseClientQuery.query(query);

		if (response.isFail()) {
			writeFailLog(response);
			return IptimeOnConnectUserInfosResponse.fail(response.getHost());
		}

		List<String> resolvedUsers =
				iptimeUsersOnConnectFilterDecorator.resolve(response.getUsersInfo());

		return iptimeBrowseConverter.from(resolvedUsers, response.getHost());
	}

	@Override
	public List<ClientResponse<OnConnectUserInfos>> queries(
			WifiBulkBrowseRequest<IptimeBrowseRequest> requests) {
		return requests.getSource().stream()
				.map(this::getIptimeOnConnectUserInfosResponse)
				.collect(Collectors.toList());
	}

	private void writeFailLog(BrowseQueryClientModel response) {
		log.warn(RESPONSE_ERROR.getLogDebugFormat(), this.getClass().getName(), response.getHost());
		slackService.sendSlackMessage(
				format(RESPONSE_ERROR.getSlackFormat(), this.getClass().getName(), response.getHost()),
				SlackChannel.ERROR);
	}

	private ClientResponse<OnConnectUserInfos> getIptimeOnConnectUserInfosResponse(
			IptimeBrowseRequest request) {
		try {
			return this.query(request);
		} catch (WifiRuntimeException e) {
			return IptimeOnConnectUserInfosResponse.fail(request.getHost());
		}
	}

	private IptimeWifiBrowseClientDto getDto(WifiBrowseRequest request) {
		Map<String, String> headers = iptimeBrowseClientHeaderGenerator.execute(request.getHost());

		return iptimeBrowseConverter.to(request, headers, request.getAuthInfo());
	}
}
