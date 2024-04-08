package com.observer.client.router.service;

import com.observer.client.router.config.ClientRouterConfig;
import com.observer.client.router.exception.ClientException;
import com.observer.client.router.service.iptime.IptimeHealthService;
import com.observer.client.router.support.dto.request.WifiHealthServiceRequest;
import com.observer.client.router.support.dto.response.RouterHealthResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@ActiveProfiles(value = {"test", "client-test"})
@SpringBootTest
@ContextConfiguration(classes = {ClientRouterConfig.class})
@TestPropertySource("classpath:application-test.yml")
class RouterHealthServiceTest {

	@Autowired IptimeHealthService iptimeHealthService;

	@Test
	@DisplayName("Iptime 공유기에서 상태 정보를 점검합니다")
	void iptimeHealthService() throws ClientException {
		// Given
		WifiHealthServiceRequest request =
				WifiHealthServiceRequest.builder().host("http://192.168.0.1").build();

		// When
		RouterHealthResponse response = iptimeHealthService.execute(request);

		// Then
		Assertions.assertEquals(HttpStatus.OK, response.getResponse());
	}
}
