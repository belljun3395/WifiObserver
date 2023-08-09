package com.wifi.observer.client.wifi.client;

import com.wifi.observer.client.wifi.client.function.QueryAble;
import com.wifi.observer.client.wifi.dto.request.WifiBulkHealthRequest;
import com.wifi.observer.client.wifi.dto.request.common.CommonWifiHealthRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import java.util.List;
import org.springframework.http.HttpStatus;

public interface WifiHealthClient
		extends QueryAble<CommonWifiHealthRequest, ClientResponse<HttpStatus>> {

	ClientResponse<HttpStatus> query(CommonWifiHealthRequest request);

	List<ClientResponse<HttpStatus>> queries(WifiBulkHealthRequest bulkHealthRequest);
}
