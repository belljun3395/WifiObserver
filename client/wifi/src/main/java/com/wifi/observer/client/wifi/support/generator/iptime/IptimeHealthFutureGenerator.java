package com.wifi.observer.client.wifi.support.generator.iptime;

import com.wifi.observer.client.wifi.dto.http.IptimeWifiHealthClientDto;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.dto.response.common.CommonHealthStatusResponse;
import com.wifi.observer.client.wifi.http.request.get.HealthClientQuery;
import com.wifi.observer.client.wifi.model.HealthQueryClientModel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IptimeHealthFutureGenerator {

	private final HealthClientQuery healthClientQuery;
	private final Executor healthAsyncExecutor;

	public IptimeHealthFutureGenerator(
			HealthClientQuery healthClientQuery,
			@Qualifier("wifiobswifihealthAsyncExecutor") Executor healthAsyncExecutor) {
		this.healthClientQuery = healthClientQuery;
		this.healthAsyncExecutor = healthAsyncExecutor;
	}

	public CompletableFuture<ClientResponse<HttpStatus>> execute(IptimeWifiHealthClientDto queryDto) {
		final String host = queryDto.getHost();
		return CompletableFuture.supplyAsync(getQueryResponseSupplier(queryDto), healthAsyncExecutor)
				.thenApply(getResponseFunction(host));
	}

	private Supplier<HealthQueryClientModel> getQueryResponseSupplier(
			IptimeWifiHealthClientDto queryDto) {
		return () -> healthClientQuery.query(queryDto);
	}

	private Function<HealthQueryClientModel, CommonHealthStatusResponse> getResponseFunction(
			String host) {
		return response ->
				CommonHealthStatusResponse.builder().response(response.getHttpStatus()).host(host).build();
	}
}
