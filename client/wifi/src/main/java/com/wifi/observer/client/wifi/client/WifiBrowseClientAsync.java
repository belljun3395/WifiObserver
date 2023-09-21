package com.wifi.observer.client.wifi.client;

import com.wifi.observer.client.wifi.client.function.QueriesAsyncAble;
import com.wifi.observer.client.wifi.dto.request.WifiBrowseRequest;
import com.wifi.observer.client.wifi.dto.request.WifiBulkBrowseRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.dto.response.OnConnectUserInfos;

public interface WifiBrowseClientAsync<
				T extends WifiBrowseRequest, R extends ClientResponse<OnConnectUserInfos>>
		extends QueriesAsyncAble<WifiBulkBrowseRequest<T>, R> {}
