package com.wifi.obs.client.wifi.client;

import com.wifi.obs.client.wifi.client.function.QueriesAsyncAble;
import com.wifi.obs.client.wifi.dto.request.WifiBrowseRequest;
import com.wifi.obs.client.wifi.dto.request.WifiBulkBrowseRequest;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.dto.response.OnConnectUserInfos;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class WifiBrowseClientAsync<T extends WifiBrowseRequest>
		implements QueriesAsyncAble<WifiBulkBrowseRequest<T>, ClientResponse<OnConnectUserInfos>> {

	protected abstract List<CompletableFuture<ClientResponse<OnConnectUserInfos>>> getFutures(
			WifiBulkBrowseRequest<T> t);
}
