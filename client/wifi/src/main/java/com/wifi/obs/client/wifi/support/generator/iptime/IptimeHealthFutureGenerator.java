package com.wifi.obs.client.wifi.support.generator.iptime;

import com.wifi.obs.client.wifi.dto.http.WifiHealthRequestElement;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.dto.response.HealthStatusResponse;
import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import com.wifi.obs.client.wifi.http.request.get.HealthClientQuery;
import com.wifi.obs.client.wifi.model.Health;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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

	public CompletableFuture<ClientResponse<HttpStatusResponse>> execute(
			WifiHealthRequestElement queryDto) {
		final String host = queryDto.getHost();
		return CompletableFuture.supplyAsync(getQueryResponseSupplier(queryDto), healthAsyncExecutor)
				.thenApply(getResponseFunction(host));
	}

	private Supplier<Health> getQueryResponseSupplier(WifiHealthRequestElement queryDto) {
		return () -> healthClientQuery.query(queryDto);
	}

	private Function<Health, HealthStatusResponse> getResponseFunction(String host) {
		return response ->
				HealthStatusResponse.builder().response(response.getHttpStatus()).host(host).build();
	}
}
