package com.observer.batch.job.browse.iptime.step;

import com.observer.batch.job.browse.exception.RouterAuthException;
import com.observer.batch.job.browse.iptime.client.BatchIptimeAuthClient;
import com.observer.client.router.exception.ClientAuthException;
import com.observer.client.router.exception.ClientException;
import com.observer.client.router.support.dto.request.iptime.IptimeAuthServiceRequest;
import com.observer.client.router.support.dto.response.RouterAuthResponse;
import com.observer.data.entity.router.RouterEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeAuthProcessor implements ItemProcessor<RouterEntity, RouterAuthResponse> {

	private final BatchIptimeAuthClient iptimeAuthService;

	@Override
	public RouterAuthResponse process(RouterEntity item) {
		final String host = item.getHost();
		final String userName = item.getCertification();
		final String password = item.getPassword();

		final IptimeAuthServiceRequest request =
				IptimeAuthServiceRequest.builder().host(host).userName(userName).password(password).build();
		try {
			return iptimeAuthService.execute(request);
		} catch (ClientAuthException | ClientException e) {
			throw new RouterAuthException(request.toString());
		}
	}
}
