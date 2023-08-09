package com.wifi.observer.client.wifi.client;

import com.wifi.observer.client.wifi.client.function.QueriesAsyncAble;
import com.wifi.observer.client.wifi.dto.request.WifiBulkHealthRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.HttpStatus;

public interface WifiHealthClientAsync<
				T extends WifiBulkHealthRequest, R extends ClientResponse<HttpStatus>>
		extends QueriesAsyncAble<T, R> {

	List<R> queriesAsync(T requests);

	List<CompletableFuture<R>> getFutures(T requests);
}
