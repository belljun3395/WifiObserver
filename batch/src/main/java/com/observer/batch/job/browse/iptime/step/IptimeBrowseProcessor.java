package com.observer.batch.job.browse.iptime.step;

import com.observer.batch.job.browse.exception.RouterBrowseException;
import com.observer.batch.job.browse.iptime.client.BatchIptimeUsersClient;
import com.observer.client.router.exception.ClientException;
import com.observer.client.router.support.dto.request.iptime.IptimeUsersServiceRequest;
import com.observer.client.router.support.dto.response.RouterAuthResponse;
import com.observer.client.router.support.dto.response.RouterUsersResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeBrowseProcessor
		implements ItemProcessor<RouterAuthResponse, RouterUsersResponse> {

	private final BatchIptimeUsersClient iptimeUsersService;

	@Override
	public RouterUsersResponse process(RouterAuthResponse item) {
		final String auth = item.getResponse().getAuth();
		final String host = item.getHost();

		final IptimeUsersServiceRequest request =
				IptimeUsersServiceRequest.builder().authInfo(auth).host(host).build();
		try {
			return iptimeUsersService.execute(request);
		} catch (ClientException e) {
			throw new RouterBrowseException(request.toString());
		}
	}
}
