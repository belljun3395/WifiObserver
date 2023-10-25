package com.wifi.obs.client.wifi.client.iptime;

import com.wifi.obs.client.wifi.dto.http.WifiBrowseRequestElement;
import com.wifi.obs.client.wifi.dto.request.WifiBulkBrowseRequest;
import com.wifi.obs.client.wifi.dto.request.iptime.IptimeBrowseRequest;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.dto.response.OnConnectUserInfos;
import com.wifi.obs.client.wifi.support.converter.iptime.IptimeBrowseConverter;
import com.wifi.obs.client.wifi.support.converter.iptime.IptimeOnConnectUsersFutureMapper;
import com.wifi.obs.client.wifi.support.generator.iptime.IptimeBrowseClientFutureGenerator;
import com.wifi.obs.client.wifi.support.generator.iptime.IptimeBrowseClientHeaderGenerator;
import com.wifi.obs.client.wifi.support.log.BulkRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeBrowseClientAsyncImpl extends IptimeBrowseClientAsync {

	private final IptimeBrowseConverter iptimeBrowseConverter;
	private final IptimeBrowseClientHeaderGenerator iptimeBrowseClientHeaderGenerator;
	private final IptimeBrowseClientFutureGenerator iptimeBrowseClientFutureGenerator;
	private final IptimeOnConnectUsersFutureMapper iptimeOnConnectUsersFutureMapper;

	@Override
	public List<ClientResponse<OnConnectUserInfos>> queriesAsync(
			@BulkRequest WifiBulkBrowseRequest<IptimeBrowseRequest> requests) {

		List<CompletableFuture<ClientResponse<OnConnectUserInfos>>> futures = getFutures(requests);

		return futures.stream()
				.map(iptimeOnConnectUsersFutureMapper::from)
				.collect(Collectors.toList());
	}

	@Override
	protected List<CompletableFuture<ClientResponse<OnConnectUserInfos>>> getFutures(
			WifiBulkBrowseRequest<IptimeBrowseRequest> requests) {
		return requests.getSource().stream()
				.map(this::getRequestElement)
				.map(this::getFuture)
				.collect(Collectors.toList());
	}

	private WifiBrowseRequestElement getRequestElement(IptimeBrowseRequest request) {
		Map<String, String> headers = iptimeBrowseClientHeaderGenerator.execute(request.getHost());

		return iptimeBrowseConverter.to(request, headers, request.getAuthInfo());
	}

	private CompletableFuture<ClientResponse<OnConnectUserInfos>> getFuture(
			WifiBrowseRequestElement data) {
		return iptimeBrowseClientFutureGenerator.execute(data);
	}
}
