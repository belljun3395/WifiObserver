package com.wifi.obs.client.wifi.client.iptime;

import com.wifi.obs.client.wifi.client.WifiBrowseClient;
import com.wifi.obs.client.wifi.dto.http.WifiBrowseRequestElement;
import com.wifi.obs.client.wifi.dto.request.WifiBulkBrowseRequest;
import com.wifi.obs.client.wifi.dto.request.iptime.IptimeBrowseRequest;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.dto.response.OnConnectUserInfos;
import com.wifi.obs.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
import com.wifi.obs.client.wifi.exception.WifiRuntimeException;
import com.wifi.obs.client.wifi.model.Users;
import java.util.List;
import java.util.stream.Collectors;

public abstract class IptimeBrowseClient
		extends WifiBrowseClient<IptimeBrowseRequest, WifiBrowseRequestElement> {

	@Override
	public ClientResponse<OnConnectUserInfos> query(IptimeBrowseRequest request)
			throws WifiRuntimeException {

		WifiBrowseRequestElement data = getRequestElement(request);

		Users response = executeQuery(data);

		if (response.isFail()) {
			writeFailLog(response);
			return IptimeOnConnectUserInfosResponse.fail(response.getHost());
		}
		return getClientResponse(response);
	}

	protected abstract ClientResponse<OnConnectUserInfos> getClientResponse(Users users);

	protected abstract void writeFailLog(Users response);

	@Override
	public List<ClientResponse<OnConnectUserInfos>> queries(
			WifiBulkBrowseRequest<IptimeBrowseRequest> requests) {
		return requests.getSource().stream().map(this::requestQuery).collect(Collectors.toList());
	}

	protected abstract ClientResponse<OnConnectUserInfos> requestQuery(IptimeBrowseRequest request);
}
