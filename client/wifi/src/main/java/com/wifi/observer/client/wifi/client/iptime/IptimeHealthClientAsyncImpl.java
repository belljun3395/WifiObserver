package com.wifi.observer.client.wifi.client.iptime;

import com.wifi.observer.client.wifi.dto.http.IptimeWifiHealthClientDto;
import com.wifi.observer.client.wifi.dto.request.common.CommonWifiHealthRequest;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBulkHealthRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.support.converter.HealthStatusResponseFutureMapper;
import com.wifi.observer.client.wifi.support.converter.iptime.IptimeWifiHealthConverter;
import com.wifi.observer.client.wifi.support.generator.iptime.IptimeHealthFutureGenerator;
import com.wifi.observer.client.wifi.support.log.BulkRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeHealthClientAsyncImpl implements IptimeHealthClientAsync {

	private final IptimeWifiHealthConverter iptimeWifiHealthConverter;
	private final IptimeHealthFutureGenerator healthFutureGenerator;
	private final HealthStatusResponseFutureMapper healthStatusResponseFutureMapper;

	@Override
	public List<ClientResponse<HttpStatus>> queriesAsync(
			@BulkRequest IptimeBulkHealthRequest requests) {

		List<CompletableFuture<ClientResponse<HttpStatus>>> futures = getFutures(requests);

		return getResponse(futures);
	}

	@Override
	public List<CompletableFuture<ClientResponse<HttpStatus>>> getFutures(
			IptimeBulkHealthRequest requests) {
		return requests.getSource().stream()
				.map(this::getDto)
				.map(this::getFuture)
				.collect(Collectors.toList());
	}

	private IptimeWifiHealthClientDto getDto(CommonWifiHealthRequest request) {
		return iptimeWifiHealthConverter.to(request.getHost());
	}

	private CompletableFuture<ClientResponse<HttpStatus>> getFuture(
			IptimeWifiHealthClientDto iptimeWifiHealthClientDto) {
		return healthFutureGenerator.execute(iptimeWifiHealthClientDto);
	}

	private List<ClientResponse<HttpStatus>> getResponse(
			List<CompletableFuture<ClientResponse<HttpStatus>>> futures) {
		return futures.stream()
				.map(healthStatusResponseFutureMapper::from)
				.collect(Collectors.toList());
	}
}
