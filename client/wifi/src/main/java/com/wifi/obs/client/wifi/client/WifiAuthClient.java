package com.wifi.obs.client.wifi.client;

import com.wifi.obs.client.wifi.client.function.CommandAble;
import com.wifi.obs.client.wifi.dto.http.WifiAuthRequestElement;
import com.wifi.obs.client.wifi.dto.request.WifiAuthRequest;
import com.wifi.obs.client.wifi.dto.response.AuthInfo;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.model.Auth;

public abstract class WifiAuthClient<T extends WifiAuthRequest, D extends WifiAuthRequestElement>
		implements CommandAble<T, ClientResponse<AuthInfo>> {

	protected abstract D getRequestElement(T request);

	protected abstract Auth executeCommand(D data);
}
