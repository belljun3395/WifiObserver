package com.observer.client.router.service;

import com.observer.client.router.exception.ClientException;
import com.observer.client.router.support.dto.request.BasicWifiServiceRequest;
import com.observer.client.router.support.dto.response.RouterUsersResponse;

public interface RouterUsersService<T extends BasicWifiServiceRequest> {

	RouterUsersResponse execute(T request) throws ClientException;
}
