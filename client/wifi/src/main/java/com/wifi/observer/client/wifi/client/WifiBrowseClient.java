package com.wifi.observer.client.wifi.client;

import com.wifi.observer.client.wifi.client.function.QueryAble;
import com.wifi.observer.client.wifi.dto.request.WifiBrowseRequest;
import com.wifi.observer.client.wifi.dto.request.WifiBulkBrowseRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.dto.response.OnConnectUserInfos;
import java.util.List;

public interface WifiBrowseClient<
				T extends WifiBrowseRequest, R extends ClientResponse<OnConnectUserInfos>>
		extends QueryAble<T, R> {

	R query(T request);

	List<R> queries(WifiBulkBrowseRequest<T> requests);
}
