package com.wifi.observer.client.wifi.support.generator.iptime;

import com.wifi.observer.client.wifi.dto.http.IptimeWifiHealthClientDto;
import com.wifi.observer.client.wifi.dto.response.common.CommonHealthStatusResponse;
import com.wifi.observer.client.wifi.http.request.get.HealthClientQuery;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
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

	public CompletableFuture<CommonHealthStatusResponse> execute(IptimeWifiHealthClientDto queryDto) {
		final String host = queryDto.getHost();
		return CompletableFuture.supplyAsync(
						() -> healthClientQuery.query(queryDto), healthAsyncExecutor)
				.thenApply(
						status -> CommonHealthStatusResponse.builder().response(status).host(host).build());
	}
}
