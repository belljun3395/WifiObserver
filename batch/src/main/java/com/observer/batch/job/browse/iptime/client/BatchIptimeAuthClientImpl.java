package com.observer.batch.job.browse.iptime.client;

import com.observer.client.router.exception.ClientAuthException;
import com.observer.client.router.exception.ClientException;
import com.observer.client.router.service.iptime.IptimeAuthService;
import com.observer.client.router.support.dto.request.iptime.IptimeAuthServiceRequest;
import com.observer.client.router.support.dto.response.RouterAuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("!batch-test")
@Component
@RequiredArgsConstructor
public class BatchIptimeAuthClientImpl implements BatchIptimeAuthClient {

	private final IptimeAuthService iptimeAuthService;

	@Override
	public RouterAuthResponse execute(IptimeAuthServiceRequest source)
			throws ClientAuthException, ClientException {
		return iptimeAuthService.execute(source);
	}
}
