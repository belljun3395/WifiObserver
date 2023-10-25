package com.wifi.obs.client.wifi.client;

import com.wifi.obs.client.wifi.client.function.QueriesAsyncAble;
import com.wifi.obs.client.wifi.dto.request.WifiBulkHealthRequest;
import com.wifi.obs.client.wifi.dto.request.WifiHostRequest;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class WifiHealthClientAsync<T extends WifiHostRequest>
		implements QueriesAsyncAble<WifiBulkHealthRequest<T>, ClientResponse<HttpStatusResponse>> {

	protected abstract List<CompletableFuture<ClientResponse<HttpStatusResponse>>> getFutures(
			WifiBulkHealthRequest<T> t);
}
