package com.wifi.observer.client.wifi.client.iptime;

import com.wifi.observer.client.wifi.client.WifiBrowseClient;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBrowseRequest;
import com.wifi.observer.client.wifi.dto.response.OnConnectUserInfos;

public interface IptimeBrowseClient
		extends WifiBrowseClient<IptimeBrowseRequest, OnConnectUserInfos> {}
