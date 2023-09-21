package com.wifi.observer.client.wifi.client;

import com.wifi.observer.client.wifi.client.function.QueriesAble;
import com.wifi.observer.client.wifi.client.function.QueryAble;
import com.wifi.observer.client.wifi.dto.request.WifiBulkHealthRequest;
import com.wifi.observer.client.wifi.dto.request.common.CommonWifiHealthRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import org.springframework.http.HttpStatus;

public interface WifiHealthClient
		extends QueryAble<CommonWifiHealthRequest, ClientResponse<HttpStatus>>,
				QueriesAble<WifiBulkHealthRequest, ClientResponse<HttpStatus>> {}
