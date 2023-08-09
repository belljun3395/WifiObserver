package com.wifi.observer.client.wifi.support.converter.iptime;

import com.wifi.observer.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
import com.wifi.observer.client.wifi.exception.WifiCompletionException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.springframework.stereotype.Component;

@Component
public class IptimeOnConnectUsersFutureMapper {

	public IptimeOnConnectUserInfosResponse from(
			CompletableFuture<IptimeOnConnectUserInfosResponse> future) {
		try {
			return future.join();
		} catch (CompletionException e) {
			throw new WifiCompletionException(e.getMessage());
		}
	}
}
