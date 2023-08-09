package com.wifi.observer.client.wifi.client;

import com.wifi.observer.client.wifi.client.function.CommandAble;
import com.wifi.observer.client.wifi.dto.request.WifiAuthClientRequest;
import com.wifi.observer.client.wifi.dto.response.AuthInfo;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;

public interface WifiAuthClient<T extends WifiAuthClientRequest, R extends AuthInfo>
		extends CommandAble<T, ClientResponse<R>> {

	ClientResponse<R> command(T request);
}
