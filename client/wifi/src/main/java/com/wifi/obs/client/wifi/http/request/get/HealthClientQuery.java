package com.wifi.obs.client.wifi.http.request.get;

import com.wifi.obs.client.wifi.dto.http.WifiHealthRequestElement;
import com.wifi.obs.client.wifi.exception.WifiURISyntaxException;
import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import com.wifi.obs.client.wifi.model.Health;
import com.wifi.obs.client.wifi.model.value.HealthQueryVO;
import com.wifi.obs.client.wifi.support.log.WifiClientTrace;
import feign.FeignException;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HealthClientQuery {

	private final WifiHealthFeignClient wifiHealthFeignClient;

	@WifiClientTrace
	public Health query(WifiHealthRequestElement source) {
		try {
			return Health.builder().statusInfo(getStatusInfo(source)).host(source.getHost()).build();
		} catch (URISyntaxException e) {
			throw new WifiURISyntaxException(source.getUrl());
		} catch (FeignException e) {
			return Health.fail(source.getHost());
		}
	}

	private HealthQueryVO getStatusInfo(WifiHealthRequestElement source) throws URISyntaxException {
		return HealthQueryVO.builder().info(executeQuery(source)).build();
	}

	private HttpStatusResponse executeQuery(WifiHealthRequestElement source)
			throws URISyntaxException {
		return HttpStatusResponse.of(
				wifiHealthFeignClient.query(new URI(source.getUrl())).getStatusCode());
	}
}
