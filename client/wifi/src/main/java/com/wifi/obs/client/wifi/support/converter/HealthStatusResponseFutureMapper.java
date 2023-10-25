package com.wifi.obs.client.wifi.support.converter;

import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.exception.WifiCompletionException;
import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.springframework.stereotype.Component;

@Component
public class HealthStatusResponseFutureMapper {

	public ClientResponse<HttpStatusResponse> from(
			CompletableFuture<ClientResponse<HttpStatusResponse>> future) {
		try {
			return future.join();
		} catch (CompletionException e) {
			throw new WifiCompletionException(e.getMessage());
		}
	}
}
