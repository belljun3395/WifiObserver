package com.observer.batch.job.browse.iptime.client;

import com.observer.client.router.exception.ClientAuthException;
import com.observer.client.router.exception.ClientException;
import com.observer.client.router.support.dto.request.iptime.IptimeAuthServiceRequest;
import com.observer.client.router.support.dto.response.RouterAuthResponse;

public interface BatchIptimeAuthClient {

	RouterAuthResponse execute(IptimeAuthServiceRequest request)
			throws ClientException, ClientAuthException;
}
