package com.wifi.observer.client.wifi.support.converter;

import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.exception.WifiCompletionException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class HealthStatusResponseFutureMapper {

	public ClientResponse<HttpStatus> from(CompletableFuture<ClientResponse<HttpStatus>> future) {
		try {
			return future.join();
		} catch (CompletionException e) {
			throw new WifiCompletionException(e.getMessage());
		}
	}
}
