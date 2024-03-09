package com.observer.batch.job.browse.iptime.step;

import com.observer.client.router.service.iptime.IptimeAuthService;
import com.observer.client.router.support.dto.request.iptime.IptimeAuthServiceRequest;
import com.observer.client.router.support.dto.response.RouterAuthResponse;
import com.observer.data.entity.router.RouterEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Import(IptimeAuthService.class)
public class IptimeAuthProcessor implements ItemProcessor<RouterEntity, RouterAuthResponse> {

	private final IptimeAuthService iptimeAuthService;

	@Override
	public RouterAuthResponse process(RouterEntity item) {
		final String host = item.getHost();
		final String userName = item.getCertification();
		final String password = item.getPassword();
		log.info("===> IptimeAuthProcessor.process() host: {}, userName: {}", host, userName);
		final IptimeAuthServiceRequest request =
				IptimeAuthServiceRequest.builder().host(host).userName(userName).password(password).build();
		return iptimeAuthService.execute(request);
	}
}
