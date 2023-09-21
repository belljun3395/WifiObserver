package com.wifi.observer.client.wifi.client;

import com.wifi.observer.client.wifi.client.function.QueriesAble;
import com.wifi.observer.client.wifi.client.function.QueryAble;
import com.wifi.observer.client.wifi.dto.request.WifiBrowseRequest;
import com.wifi.observer.client.wifi.dto.request.WifiBulkBrowseRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.dto.response.OnConnectUserInfos;

public interface WifiBrowseClient<T extends WifiBrowseRequest, R extends OnConnectUserInfos>
		extends QueryAble<T, ClientResponse<R>>,
				QueriesAble<WifiBulkBrowseRequest<T>, ClientResponse<R>> {}
