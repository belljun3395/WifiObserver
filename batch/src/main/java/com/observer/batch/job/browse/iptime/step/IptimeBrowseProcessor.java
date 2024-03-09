package com.observer.batch.job.browse.iptime.step;

import com.observer.client.router.service.iptime.IptimeUsersService;
import com.observer.client.router.support.dto.request.iptime.IptimeUsersServiceRequest;
import com.observer.client.router.support.dto.response.RouterAuthResponse;
import com.observer.client.router.support.dto.response.RouterUsersResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Import(IptimeUsersService.class)
public class IptimeBrowseProcessor
		implements ItemProcessor<RouterAuthResponse, RouterUsersResponse> {

	private final IptimeUsersService iptimeUsersService;

	@Override
	public RouterUsersResponse process(RouterAuthResponse item) {
		final String auth = item.getResponse().getAuth();
		final String host = item.getHost();
		log.info("===> IptimeBrowseProcessor.process() host: {}", host);
		final IptimeUsersServiceRequest request =
				IptimeUsersServiceRequest.builder().authInfo(auth).host(host).build();
		return iptimeUsersService.execute(request);
	}
}