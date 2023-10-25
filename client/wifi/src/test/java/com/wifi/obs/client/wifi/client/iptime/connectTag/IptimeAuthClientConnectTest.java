package com.wifi.obs.client.wifi.client.iptime.connectTag;

import static org.assertj.core.api.Assertions.assertThat;

import com.wifi.obs.client.wifi.WifiClientConfig;
import com.wifi.obs.client.wifi.client.iptime.IptimeAuthClient;
import com.wifi.obs.client.wifi.dto.request.iptime.IptimeAuthRequest;
import com.wifi.obs.client.wifi.dto.response.AuthInfo;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.test.util.CookieResource;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(classes = {WifiClientConfig.class, CookieResource.class})
@TestPropertySource("classpath:application-test.yml")
@DisplayName("IPTIME 공유기 쿠키 획득 연결 테스트")
@Tag("connect")
class IptimeAuthClientConnectTest {

	@Value("${test.host}")
	String host;

	@Value("${test.userName}")
	String userName;

	@Value("${test.password}")
	String password;

	@Autowired IptimeAuthClient iptimeAuthClient;
	@Autowired CookieResource cookieResource;

	@BeforeEach
	void setUp() {
		MDC.put("request_id", UUID.randomUUID().toString());
	}

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
		// when
		ClientResponse<AuthInfo> response = iptimeAuthClient.command(request);

		// then

		assertThat(response.getHost()).contains(host);
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
		assertThat(response.getHost()).contains(host);
		assertThat(response.getResponse()).isEmpty();
	}
}
