package com.wifi.observer.client.wifi.client.iptime;

import com.wifi.observer.client.wifi.client.WifiBrowseClientAsync;
import com.wifi.observer.client.wifi.dto.http.IptimeWifiBrowseClientDto;
import com.wifi.observer.client.wifi.dto.request.WifiBulkBrowseRequest;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBrowseRequest;
import com.wifi.observer.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
import com.wifi.observer.client.wifi.support.converter.iptime.IptimeBrowseConverter;
import com.wifi.observer.client.wifi.support.converter.iptime.IptimeOnConnectUsersFutureMapper;
import com.wifi.observer.client.wifi.support.generator.iptime.IptimeBrowseClientFutureGenerator;
import com.wifi.observer.client.wifi.support.generator.iptime.IptimeBrowseClientHeaderGenerator;
import com.wifi.observer.client.wifi.support.log.BulkRequest;
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
public class IptimeBrowseClientAsyncImpl
		implements WifiBrowseClientAsync<IptimeBrowseRequest, IptimeOnConnectUserInfosResponse> {

	private final IptimeBrowseConverter iptimeBrowseClientConverter;
	private final IptimeBrowseClientHeaderGenerator iptimeBrowseClientHeaderGenerator;
	private final IptimeBrowseClientFutureGenerator iptimeBrowseClientFutureGenerator;
	private final IptimeOnConnectUsersFutureMapper iptimeOnConnectUsersFutureMapper;

	@Override
	public List<IptimeOnConnectUserInfosResponse> queriesAsync(
			@BulkRequest WifiBulkBrowseRequest<IptimeBrowseRequest> requests) {
		List<CompletableFuture<IptimeOnConnectUserInfosResponse>> futures = getFutures(requests);

		return futures.stream()
				.map(iptimeOnConnectUsersFutureMapper::from)
				.collect(Collectors.toList());
	}

	@Override
	public List<CompletableFuture<IptimeOnConnectUserInfosResponse>> getFutures(
			WifiBulkBrowseRequest<IptimeBrowseRequest> requests) {

		return requests.getSource().stream()
				.map(this::getDto)
				.map(this::gerFuture)
				.collect(Collectors.toList());
	}

	private IptimeWifiBrowseClientDto getDto(IptimeBrowseRequest request) {
		Map<String, String> headers = iptimeBrowseClientHeaderGenerator.execute(request.getHost());

		return iptimeBrowseClientConverter.to(request, headers, request.getAuthInfo());
	}

	private CompletableFuture<IptimeOnConnectUserInfosResponse> gerFuture(
			IptimeWifiBrowseClientDto queryDto) {

		return iptimeBrowseClientFutureGenerator.execute(queryDto);
	}
}