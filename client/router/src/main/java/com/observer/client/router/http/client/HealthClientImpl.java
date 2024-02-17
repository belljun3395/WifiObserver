package com.observer.client.router.http.client;

import com.observer.client.router.http.dto.http.RouterConnectStatus;
import com.observer.client.router.http.dto.http.iptime.IptimeWifiHealthClientDto;
import feign.FeignException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HealthClientImpl implements HealthClient<IptimeWifiHealthClientDto> {

	private final WifiHealthFeignClient wifiHealthFeignClient;

	@Override
	public RouterConnectStatus execute(IptimeWifiHealthClientDto source) throws IOException {
		try {
			HttpStatus statusCode = wifiHealthFeignClient.query(new URI(source.getUrl())).getStatusCode();
			return RouterConnectStatus.builder().status(statusCode).build();
		} catch (URISyntaxException | FeignException e) {
			throw new IOException(e);
		}
	}
}
