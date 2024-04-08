package com.observer.batch.job.browse.iptime.client;

import com.observer.client.router.exception.ClientException;
import com.observer.client.router.service.iptime.IptimeUsersService;
import com.observer.client.router.support.dto.request.iptime.IptimeUsersServiceRequest;
import com.observer.client.router.support.dto.response.RouterUsersResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("!batch-test")
@Component
@RequiredArgsConstructor
public class BatchIptimeUsersClientImpl implements BatchIptimeUsersClient {

	private final IptimeUsersService iptimeUsersService;

	@Override
	public RouterUsersResponse execute(IptimeUsersServiceRequest request) throws ClientException {
		return iptimeUsersService.execute(request);
	}
}
