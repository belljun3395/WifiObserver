package com.wifi.obs.infra.batch.job.browse.iptime.step;

import com.wifi.observer.client.wifi.client.iptime.IptimeBrowseClientImpl;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBrowseRequest;
import com.wifi.observer.client.wifi.dto.response.AuthInfo;
import com.wifi.observer.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeBrowseProcessor
		implements ItemProcessor<AuthInfo, IptimeOnConnectUserInfosResponse> {

	private final IptimeBrowseClientImpl iptimeBrowseClient;

	@Override
	public IptimeOnConnectUserInfosResponse process(AuthInfo item) {
		IptimeBrowseRequest request =
				IptimeBrowseRequest.builder().authInfo(item.getInfo()).host(item.getHost()).build();
		log.debug(">>> request : {}", request);

		IptimeOnConnectUserInfosResponse browseInfo = iptimeBrowseClient.query(request);

		if (browseInfo.getResponse().isEmpty()) {
			log.error("browseInfo is empty host : {}", browseInfo.getHost());
			return null;
		}

		return browseInfo;
	}
}
