package com.wifi.observer.client.wifi.client.iptime;

import com.wifi.observer.client.wifi.client.WifiHealthClientAsync;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBulkHealthRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import org.springframework.http.HttpStatus;

public interface IptimeHealthClientAsync
		extends WifiHealthClientAsync<IptimeBulkHealthRequest, ClientResponse<HttpStatus>> {}
