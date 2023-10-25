package com.wifi.obs.client.wifi.client;

import com.wifi.obs.client.wifi.client.function.QueriesAble;
import com.wifi.obs.client.wifi.client.function.QueryAble;
import com.wifi.obs.client.wifi.dto.http.WifiHealthRequestElement;
import com.wifi.obs.client.wifi.dto.request.WifiBulkHealthRequest;
import com.wifi.obs.client.wifi.dto.request.WifiHealthRequest;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import com.wifi.obs.client.wifi.model.Health;

public abstract class WifiHealthClient<
				T extends WifiHealthRequest, D extends WifiHealthRequestElement>
		implements QueryAble<T, ClientResponse<HttpStatusResponse>>,
				QueriesAble<WifiBulkHealthRequest<T>, ClientResponse<HttpStatusResponse>> {

	protected abstract D getRequestElement(T request);

	protected abstract Health executeQuery(D data);
}
