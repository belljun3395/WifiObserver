package com.wifi.observer.client.wifi.support.converter;

import com.wifi.observer.client.wifi.dto.response.common.CommonHealthStatusResponse;
import com.wifi.observer.client.wifi.exception.WifiCompletionException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.springframework.stereotype.Component;

@Component
public class HealthStatusResponseFutureMapper {

	public CommonHealthStatusResponse from(CompletableFuture<CommonHealthStatusResponse> future) {
		try {
			return future.join();
		} catch (CompletionException e) {
			throw new WifiCompletionException(e.getMessage());
		}
	}
}
