package com.wifi.obs.client.wifi.client;

import com.wifi.obs.client.wifi.client.function.QueriesAble;
import com.wifi.obs.client.wifi.client.function.QueryAble;
import com.wifi.obs.client.wifi.dto.http.WifiBrowseRequestElement;
import com.wifi.obs.client.wifi.dto.request.WifiBrowseRequest;
import com.wifi.obs.client.wifi.dto.request.WifiBulkBrowseRequest;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.dto.response.OnConnectUserInfos;
import java.util.List;

public abstract class WifiBrowseClient<
				T extends WifiBrowseRequest, D extends WifiBrowseRequestElement>
		implements QueryAble<T, ClientResponse<OnConnectUserInfos>>,
				QueriesAble<WifiBulkBrowseRequest<T>, ClientResponse<OnConnectUserInfos>> {

	protected abstract D getRequestElement(T request);

	protected abstract List<String> executeQuery(D data);
}
