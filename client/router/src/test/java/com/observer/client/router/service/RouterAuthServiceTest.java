package com.observer.client.router.service;

import com.observer.client.router.config.ClientRouterConfig;
import com.observer.client.router.exception.ClientAuthException;
import com.observer.client.router.exception.ClientException;
import com.observer.client.router.service.iptime.IptimeAuthService;
import com.observer.client.router.support.dto.request.iptime.IptimeAuthServiceRequest;
import com.observer.client.router.support.dto.response.RouterAuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@ActiveProfiles(value = {"test", "client-test"})
@SpringBootTest
@ContextConfiguration(classes = {ClientRouterConfig.class})
@TestPropertySource("classpath:application-test.yml")
class RouterAuthServiceTest {

	@Autowired IptimeAuthService iptimeAuthService;

	static final String COOKIE_VALUE = "2Lr3BFTO4DCFeGQF";

	@Test
	@DisplayName("Iptime 공유기에 로그인하고 쿠키 값을 획득합니다")
	void iptimeAuthService() throws ClientException, ClientAuthException {
		// Given
		IptimeAuthServiceRequest request =
				IptimeAuthServiceRequest.builder().userName("userName").password("password").build();

		// When
		RouterAuthResponse response = iptimeAuthService.execute(request);

		// Then
		Assertions.assertEquals(COOKIE_VALUE, response.getResponse().getAuth());
	}
}
