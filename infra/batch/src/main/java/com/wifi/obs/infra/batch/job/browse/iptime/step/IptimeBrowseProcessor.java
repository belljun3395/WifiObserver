package com.wifi.obs.infra.batch.job.browse.iptime.step;

import com.wifi.obs.client.wifi.client.iptime.IptimeBrowseClient;
import com.wifi.obs.client.wifi.dto.request.iptime.IptimeBrowseRequest;
import com.wifi.obs.client.wifi.dto.response.AuthInfo;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.dto.response.OnConnectUserInfos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeBrowseProcessor
		implements ItemProcessor<ClientResponse<AuthInfo>, ClientResponse<OnConnectUserInfos>> {

	private final IptimeBrowseClient iptimeBrowseClient;

	@Override
	public ClientResponse<OnConnectUserInfos> process(ClientResponse<AuthInfo> item) {
		IptimeBrowseRequest request =
				IptimeBrowseRequest.builder()
						.authInfo(item.getResponse().get().getInfo())
						.host(item.getHost())
						.build();
		log.debug(">>> request : {}", request);

		ClientResponse<OnConnectUserInfos> browseInfo = iptimeBrowseClient.query(request);

		if (browseInfo.getResponse().isEmpty()) {
			log.error("browseInfo is empty host : {}", browseInfo.getHost());
			return null;
		}

		return browseInfo;
	}
}
