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

		List<String> connectedUsers = executeQuery(data);

		if (connectedUsers.isEmpty()) {
			writeFailLog(data.getHost());
			return IptimeOnConnectUserInfosResponse.fail(data.getHost());
		}

		Users users = getUsers(data.getHost(), connectedUsers);

		return getClientResponse(users);
	}

	private Users getUsers(String host, List<String> users) {
		return Users.builder().users(users).host(host).build();
	}

	protected abstract ClientResponse<OnConnectUserInfos> getClientResponse(Users users);

	protected abstract void writeFailLog(String host);

	@Override
	public List<ClientResponse<OnConnectUserInfos>> queries(
			WifiBulkBrowseRequest<IptimeBrowseRequest> requests) {
		return requests.getSource().stream().map(this::requestQuery).collect(Collectors.toList());
	}

	protected abstract ClientResponse<OnConnectUserInfos> requestQuery(IptimeBrowseRequest request);
}
