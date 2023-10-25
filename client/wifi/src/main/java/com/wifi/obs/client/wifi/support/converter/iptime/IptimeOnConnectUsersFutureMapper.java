package com.wifi.obs.client.wifi.support.converter.iptime;

import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.dto.response.OnConnectUserInfos;
import com.wifi.obs.client.wifi.exception.WifiCompletionException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.springframework.stereotype.Component;

@Component
public class IptimeOnConnectUsersFutureMapper {

	public ClientResponse<OnConnectUserInfos> from(
			CompletableFuture<ClientResponse<OnConnectUserInfos>> future) {
		try {
			return future.join();
		} catch (CompletionException e) {
			throw new WifiCompletionException(e.getMessage());
		}
	}
}
