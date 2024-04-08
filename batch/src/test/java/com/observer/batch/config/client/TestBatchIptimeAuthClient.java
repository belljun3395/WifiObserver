package com.observer.batch.config.client;

import com.observer.batch.job.browse.iptime.client.BatchIptimeAuthClient;
import com.observer.client.router.exception.ClientAuthException;
import com.observer.client.router.exception.ClientException;
import com.observer.client.router.support.dto.request.iptime.IptimeAuthServiceRequest;
import com.observer.client.router.support.dto.response.RouterAuthInfo;
import com.observer.client.router.support.dto.response.RouterAuthResponse;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class TestBatchIptimeAuthClient implements BatchIptimeAuthClient {

	static final String COOKIE_VALUE = "2Lr3BFTO4DCFeGQF";

	@Override
	public RouterAuthResponse execute(IptimeAuthServiceRequest request)
			throws ClientException, ClientAuthException {
		return RouterAuthResponse.builder()
				.response(RouterAuthInfo.builder().auth(COOKIE_VALUE).build())
				.build();
	}
}
