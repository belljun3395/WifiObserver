package com.wifi.obs.client.wifi.client.iptime;

import com.wifi.obs.client.wifi.client.WifiAuthClient;
import com.wifi.obs.client.wifi.dto.http.WifiAuthRequestElement;
import com.wifi.obs.client.wifi.dto.request.iptime.IptimeAuthRequest;
import com.wifi.obs.client.wifi.dto.response.AuthInfo;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.dto.response.iptime.IptimeAuthResponse;
import com.wifi.obs.client.wifi.model.Auth;

public abstract class IptimeAuthClient
		extends WifiAuthClient<IptimeAuthRequest, WifiAuthRequestElement> {

	@Override
	public ClientResponse<AuthInfo> command(IptimeAuthRequest request) {

		WifiAuthRequestElement data = getRequestElement(request);

		Auth response = executeCommand(data);

		if (response.isFail()) {
			writeFailLog(response);
			return IptimeAuthResponse.fail(response.getHost());
		}

		return getClientResponse(response);
	}

	protected abstract ClientResponse<AuthInfo> getClientResponse(Auth response);

	protected abstract void writeFailLog(Auth response);
}
