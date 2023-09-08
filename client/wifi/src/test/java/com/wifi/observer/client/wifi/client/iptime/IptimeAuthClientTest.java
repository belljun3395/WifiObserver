package com.wifi.observer.client.wifi.client.iptime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.wifi.observer.client.wifi.WifiClientConfig;
import com.wifi.observer.client.wifi.dto.http.IptimeWifiAuthClientDto;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeAuthRequest;
import com.wifi.observer.client.wifi.dto.response.AuthInfo;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.http.request.post.AuthClientCommand;
import com.wifi.observer.test.util.CookieResource;
import com.wifi.observer.test.util.DocumentResource;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(
		classes = {WifiClientConfig.class, CookieResource.class, DocumentResource.class})
@TestPropertySource("classpath:application-test.yml")
@DisplayName("IPTIME 공유기 쿠키 획득 테스트")
class IptimeAuthClientTest {

	static String host = "host";
	static String userName = "userName";
	static String password = "password";

	@InjectMocks @Autowired IptimeAuthClientImpl iptimeAuthClient;
	@MockBean AuthClientCommand authClientCommand;

	@Autowired CookieResource cookieResource;
	@Autowired DocumentResource documentResource;

	@AfterEach
	void clean() {
		MDC.clear();
	}

	@Test
	@DisplayName("획득 성공 테스트")
	void iptimeGetCookieTest() {
		// given
		IptimeAuthRequest request =
				IptimeAuthRequest.builder().host(host).userName(userName).password(password).build();

		when(authClientCommand.command(any(IptimeWifiAuthClientDto.class)))
				.thenReturn(Optional.of(documentResource.getAuthDocument()));

		// when
		ClientResponse<AuthInfo> response = iptimeAuthClient.command(request);

		// then

		assertThat(response.getHost()).isEqualTo(host);
		assertThat(response.getResponse()).isNotNull();
	}

	@Test
	@DisplayName("획득 실패 테스트")
	void iptimeGetCookieFailTest() {

		// given
		String host = "wrong host";
		IptimeAuthRequest request =
				IptimeAuthRequest.builder().host(host).userName(userName).password(password).build();

		// when
		ClientResponse<AuthInfo> response = iptimeAuthClient.command(request);

		// then
		assertThat(response.getHost()).isEqualTo(host);
		assertThat(response.getResponse()).isEmpty();
	}
}
