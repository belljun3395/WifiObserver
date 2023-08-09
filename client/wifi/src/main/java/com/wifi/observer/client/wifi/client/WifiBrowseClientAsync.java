package com.wifi.observer.client.wifi.client;

import com.wifi.observer.client.wifi.client.function.QueriesAsyncAble;
import com.wifi.observer.client.wifi.dto.request.WifiBrowseRequest;
import com.wifi.observer.client.wifi.dto.request.WifiBulkBrowseRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.dto.response.OnConnectUserInfos;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface WifiBrowseClientAsync<
				T extends WifiBrowseRequest, R extends ClientResponse<OnConnectUserInfos>>
		extends QueriesAsyncAble<WifiBulkBrowseRequest<T>, R> {

	List<R> queriesAsync(WifiBulkBrowseRequest<T> requests);

	List<CompletableFuture<R>> getFutures(WifiBulkBrowseRequest<T> requests);
}
