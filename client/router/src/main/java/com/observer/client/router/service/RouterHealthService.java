package com.observer.client.router.service;

import com.observer.client.router.exception.ClientException;
import com.observer.client.router.support.dto.request.WifiHealthServiceRequest;
import com.observer.client.router.support.dto.response.RouterHealthResponse;

public interface RouterHealthService {

	RouterHealthResponse execute(WifiHealthServiceRequest request) throws ClientException;
}
