package com.wifi.observer.client.wifi.client.iptime;

import com.wifi.observer.client.wifi.client.WifiAuthClient;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeAuthRequest;
import com.wifi.observer.client.wifi.dto.response.AuthInfo;

public interface IptimeAuthClient extends WifiAuthClient<IptimeAuthRequest, AuthInfo> {}
