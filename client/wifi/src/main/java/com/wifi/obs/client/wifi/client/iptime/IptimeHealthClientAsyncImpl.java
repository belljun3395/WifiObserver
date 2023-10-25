package com.wifi.obs.client.wifi.client.iptime;

import com.wifi.obs.client.wifi.dto.http.WifiHealthRequestElement;
import com.wifi.obs.client.wifi.dto.request.WifiBulkHealthRequest;
import com.wifi.obs.client.wifi.dto.request.WifiHostRequest;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import com.wifi.obs.client.wifi.support.converter.HealthStatusResponseFutureMapper;
import com.wifi.obs.client.wifi.support.converter.iptime.IptimeWifiHealthConverter;
import com.wifi.obs.client.wifi.support.generator.iptime.IptimeHealthFutureGenerator;
import com.wifi.obs.client.wifi.support.log.BulkRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeHealthClientAsyncImpl extends IptimeHealthClientAsync {

	private final IptimeWifiHealthConverter iptimeWifiHealthConverter;
	private final IptimeHealthFutureGenerator healthFutureGenerator;
	private final HealthStatusResponseFutureMapper healthStatusResponseFutureMapper;

	@Override
	public List<ClientResponse<HttpStatusResponse>> queriesAsync(
			@BulkRequest WifiBulkHealthRequest<WifiHostRequest> requests) {

		List<CompletableFuture<ClientResponse<HttpStatusResponse>>> futures = getFutures(requests);

		return futures.stream()
				.map(healthStatusResponseFutureMapper::from)
				.collect(Collectors.toList());
	}

	@Override
	protected List<CompletableFuture<ClientResponse<HttpStatusResponse>>> getFutures(
			WifiBulkHealthRequest<WifiHostRequest> requests) {
		return requests.getSource().stream()
				.map(this::getRequestElement)
				.map(this::getFuture)
				.collect(Collectors.toList());
	}

	private WifiHealthRequestElement getRequestElement(WifiHostRequest request) {
		return iptimeWifiHealthConverter.to(request.getHost());
	}

	private CompletableFuture<ClientResponse<HttpStatusResponse>> getFuture(
			WifiHealthRequestElement data) {
		return healthFutureGenerator.execute(data);
	}
}
