package com.wifi.obs.client.wifi.client.iptime;

import com.wifi.obs.client.wifi.client.WifiHealthClient;
import com.wifi.obs.client.wifi.dto.http.WifiHealthRequestElement;
import com.wifi.obs.client.wifi.dto.request.WifiBulkHealthRequest;
import com.wifi.obs.client.wifi.dto.request.WifiHostRequest;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import com.wifi.obs.client.wifi.model.Health;
import com.wifi.obs.client.wifi.support.log.WifiClientTrace;
import java.util.List;
import java.util.stream.Collectors;

public abstract class IptimeHealthClient
		extends WifiHealthClient<WifiHostRequest, WifiHealthRequestElement> {
	@Override
	@WifiClientTrace
	public ClientResponse<HttpStatusResponse> query(WifiHostRequest request) {

		WifiHealthRequestElement data = getRequestElement(request);

		Health response = executeQuery(data);

		if (response.isFail()) {
			writeFailLog(response);
		}

		return getClientResponse(response);
	}

	protected abstract ClientResponse<HttpStatusResponse> getClientResponse(Health response);

	protected abstract void writeFailLog(Health response);

	@Override
	public List<ClientResponse<HttpStatusResponse>> queries(
			WifiBulkHealthRequest<WifiHostRequest> requests) {
		return requests.getSource().stream().map(this::query).collect(Collectors.toList());
	}
}
