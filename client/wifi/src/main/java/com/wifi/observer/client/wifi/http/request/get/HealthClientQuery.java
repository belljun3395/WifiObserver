package com.wifi.observer.client.wifi.http.request.get;

import com.wifi.observer.client.wifi.dto.http.IptimeWifiHealthClientDto;
import com.wifi.observer.client.wifi.exception.WifiURISyntaxException;
import com.wifi.observer.client.wifi.model.HealthQueryClientModel;
import com.wifi.observer.client.wifi.model.info.HealthQueryInfo;
import com.wifi.observer.client.wifi.support.log.WifiClientTrace;
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
	public HealthQueryClientModel query(IptimeWifiHealthClientDto source) {
		try {
			return HealthQueryClientModel.builder()
					.statusInfo(
							HealthQueryInfo.builder()
									.info(wifiHealthFeignClient.query(new URI(source.getURL())).getStatusCode())
									.build())
					.host(source.getHost())
					.build();
		} catch (URISyntaxException e) {
			throw new WifiURISyntaxException(source.getURL());
		} catch (FeignException e) {
			return HealthQueryClientModel.fail(source.getHost());
		}
	}
}
