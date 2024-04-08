package com.observer.batch.job.browse.iptime.client;

import com.observer.client.router.exception.ClientException;
import com.observer.client.router.support.dto.request.iptime.IptimeUsersServiceRequest;
import com.observer.client.router.support.dto.response.RouterUsersResponse;

public interface BatchIptimeUsersClient {

	RouterUsersResponse execute(IptimeUsersServiceRequest request) throws ClientException;
}
