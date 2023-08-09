package com.wifi.observer.client.wifi.client.iptime;

import com.wifi.observer.client.wifi.client.WifiBrowseClient;
import com.wifi.observer.client.wifi.dto.http.IptimeWifiBrowseClientDto;
import com.wifi.observer.client.wifi.dto.request.WifiBrowseRequest;
import com.wifi.observer.client.wifi.dto.request.WifiBulkBrowseRequest;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBrowseRequest;
import com.wifi.observer.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
import com.wifi.observer.client.wifi.exception.WifiRuntimeException;
import com.wifi.observer.client.wifi.http.request.get.BrowseClientQuery;
import com.wifi.observer.client.wifi.support.converter.iptime.IptimeBrowseConverter;
import com.wifi.observer.client.wifi.support.generator.iptime.IptimeBrowseClientHeaderGenerator;
import com.wifi.observer.client.wifi.util.resolver.users.IptimeUsersOnConnectFilterDecorator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeBrowseClientImpl
		implements WifiBrowseClient<IptimeBrowseRequest, IptimeOnConnectUserInfosResponse> {

	private final IptimeBrowseConverter iptimeBrowseConverter;
	private final IptimeBrowseClientHeaderGenerator iptimeBrowseClientHeaderGenerator;
	private final IptimeUsersOnConnectFilterDecorator iptimeUsersOnConnectFilterDecorator;
	private final BrowseClientQuery browseClientQuery;

	@Override
	public IptimeOnConnectUserInfosResponse query(IptimeBrowseRequest request)
			throws WifiRuntimeException {

		IptimeWifiBrowseClientDto query = getDto(request);

		Optional<Document> response = browseClientQuery.query(query);

		if (response.isEmpty()) {
			return IptimeOnConnectUserInfosResponse.fail(request.getHost());
		}

		return iptimeBrowseConverter.from(
				iptimeUsersOnConnectFilterDecorator.resolve(response), request.getHost());
	}

	@Override
	public List<IptimeOnConnectUserInfosResponse> queries(
			WifiBulkBrowseRequest<IptimeBrowseRequest> requests) {
		return requests.getSource().stream()
				.map(this::getIptimeOnConnectUserInfosResponse)
				.collect(Collectors.toList());
	}

	private IptimeOnConnectUserInfosResponse getIptimeOnConnectUserInfosResponse(
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
