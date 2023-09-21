package com.wifi.observer.client.wifi.client.iptime;

import com.wifi.observer.client.wifi.client.WifiBrowseClientAsync;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBrowseRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.dto.response.OnConnectUserInfos;

public interface IptimeBrowseClientAsync
		extends WifiBrowseClientAsync<IptimeBrowseRequest, ClientResponse<OnConnectUserInfos>> {}
