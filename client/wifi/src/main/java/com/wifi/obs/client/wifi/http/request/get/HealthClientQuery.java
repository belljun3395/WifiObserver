package com.wifi.obs.client.wifi.http.request.get;

import com.wifi.obs.client.wifi.dto.http.WifiHealthRequestElement;
import com.wifi.obs.client.wifi.exception.WifiURISyntaxException;
import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import com.wifi.obs.client.wifi.support.log.WifiClientTrace;
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
	public HttpStatusResponse query(WifiHealthRequestElement source) {
		try {
			return HttpStatusResponse.of(execute(source));
		} catch (URISyntaxException e) {
			throw new WifiURISyntaxException(source.getUrl());
		} catch (FeignException e) {
			return HttpStatusResponse.of(HttpStatus.BAD_REQUEST);
		}
	}

	private HttpStatus execute(WifiHealthRequestElement source) throws URISyntaxException {
		return wifiHealthFeignClient.query(new URI(source.getUrl())).getStatusCode();
	}
}
