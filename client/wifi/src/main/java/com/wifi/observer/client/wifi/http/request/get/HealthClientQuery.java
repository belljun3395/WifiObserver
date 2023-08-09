package com.wifi.observer.client.wifi.http.request.get;

import com.wifi.observer.client.wifi.dto.http.IptimeWifiHealthClientDto;
import com.wifi.observer.client.wifi.exception.WifiURISyntaxException;
import com.wifi.observer.client.wifi.support.log.WifiClientTrace;
import feign.FeignException;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HealthClientQuery {

	private final WifiHealthFeignClient wifiHealthFeignClient;

	@WifiClientTrace
	public HttpStatus query(IptimeWifiHealthClientDto source) {
		try {
			return wifiHealthFeignClient.query(new URI(source.getURL())).getStatusCode();
		} catch (URISyntaxException e) {
			throw new WifiURISyntaxException(source.getURL());
		} catch (FeignException e) {
			return HttpStatus.BAD_REQUEST;
		}
	}
}
