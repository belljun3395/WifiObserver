package com.wifi.obs.infra.batch.job.browse.iptime.step;

import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceEntity;
import com.wifi.observer.client.wifi.client.iptime.IptimeAuthClientImpl;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeAuthRequest;
import com.wifi.observer.client.wifi.dto.response.AuthInfo;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeAuthProcessor implements ItemProcessor<WifiServiceEntity, AuthInfo> {

	private final IptimeAuthClientImpl iptimeAuthClient;

	@Override
	@Transactional(readOnly = true, transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public AuthInfo process(WifiServiceEntity item) {
		WifiAuthEntity auth = item.getWifiAuthEntity();
		log.debug(">>> auth : {}", auth);

		IptimeAuthRequest request = getRequest(auth);
		ClientResponse<AuthInfo> authInfo = iptimeAuthClient.command(request);

		if (authInfo.getResponse().isEmpty()) {
			log.error("authInfo is empty. host: {}", authInfo.getHost());
			return null;
		}

		return authInfo.getResponse().get();
	}

	private IptimeAuthRequest getRequest(WifiAuthEntity auth) {
		return IptimeAuthRequest.builder()
				.host(auth.getHost())
				.userName(auth.getCertification())
				.password(auth.getPassword())
				.build();
	}
}
