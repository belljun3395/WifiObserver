package com.observer.client.router.config;

import com.observer.client.router.http.client.HealthClient;
import com.observer.client.router.http.dto.http.RouterConnectStatus;
import com.observer.client.router.http.dto.http.iptime.IptimeWifiHealthClientDto;
import java.io.IOException;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.HttpStatus;

@TestComponent
public class TestIptimeHealthClient implements HealthClient<IptimeWifiHealthClientDto> {

	@Override
	public RouterConnectStatus execute(IptimeWifiHealthClientDto source) throws IOException {
		HttpStatus statusCode = HttpStatus.OK;

		return RouterConnectStatus.builder().status(statusCode).build();
	}
}
