package com.observer.client.router.service;

import com.observer.client.router.support.dto.request.BasicWifiServiceRequest;
import com.observer.client.router.support.dto.response.RouterAuthResponse;

public interface RouterAuthService<T extends BasicWifiServiceRequest> {

	RouterAuthResponse execute(T request);
}
