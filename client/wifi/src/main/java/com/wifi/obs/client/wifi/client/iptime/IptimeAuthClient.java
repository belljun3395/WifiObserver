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

		String cookie = executeCommand(data);

		if (cookie.isEmpty()) {
			writeFailLog(data.getHost());
			return IptimeAuthResponse.fail(data.getHost());
		}

		Auth auth = getAuth(data.getHost(), cookie);

		return getClientResponse(auth);
	}

	private Auth getAuth(String host, String cookie) {
		return Auth.builder().authInfo(cookie).host(host).build();
	}

	protected abstract ClientResponse<AuthInfo> getClientResponse(Auth response);

	protected abstract void writeFailLog(String host);
}
