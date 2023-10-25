package com.wifi.obs.client.wifi.client.iptime.connectTag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wifi.obs.client.wifi.WifiClientConfig;
import com.wifi.obs.client.wifi.client.iptime.IptimeBrowseClientAsync;
import com.wifi.obs.client.wifi.dto.request.WifiBulkBrowseRequest;
import com.wifi.obs.client.wifi.dto.request.iptime.IptimeBrowseRequest;
import com.wifi.obs.client.wifi.dto.request.iptime.IptimeBulkBrowseRequest;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.dto.response.OnConnectUserInfos;
import com.wifi.obs.test.util.CookieResource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
@DisplayName("비동기 IPTIME 공유기 조회 연결 테스트")
@Tag("connect")
class IptimeBrowseClientAsyncConnectTest {

	private static final int BULK_COUNT = 10;

	@Value("${test.host}")
	String host;

	@Value("${test.userName}")
	String userName;

	@Value("${test.password}")
	String password;

	@Value("${test.hosts}")
	String[] hosts;

	@Value("${test.userNames}")
	String[] userNames;

	@Value("${test.passwords}")
	String[] passwords;

	@Autowired IptimeBrowseClientAsync iptimeBrowseClientAsync;

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
	@DisplayName("벌크 접속자 조회 성공 테스트")
	void browseBulkIptimeOnConnectUsersTest() {
		// given
		List<IptimeBrowseRequest> requests = new ArrayList<>();
		for (int i = 0; i < BULK_COUNT; i++) {
			requests.add(
					IptimeBrowseRequest.builder().authInfo(cookieResource.getCookie()).host(host).build());
		}
		WifiBulkBrowseRequest<IptimeBrowseRequest> bulkOnConnectRequest =
				IptimeBulkBrowseRequest.of(requests);

		// when
		List<ClientResponse<OnConnectUserInfos>> responses =
				iptimeBrowseClientAsync.queriesAsync(bulkOnConnectRequest);

		// then
		responses.stream()
				.map(ClientResponse::getResponse)
				.filter(Optional::isPresent)
				.forEach(response -> assertThat(response.get().getUsers()).isNotEmpty());
	}

	@Test
	@DisplayName("벌크 접속자 조회 실패 포함 테스트")
	void browseBulkIncludeFailIptimeOnConnectUsersTest() {

		// given
		List<IptimeBrowseRequest> requests = new ArrayList<>();
		IptimeBrowseRequest failRequest =
				IptimeBrowseRequest.builder().authInfo("failCookie").host("failHost").build();
		requests.add(failRequest);
		for (int i = 0; i < BULK_COUNT; i++) {
			requests.add(
					IptimeBrowseRequest.builder().authInfo(cookieResource.getCookie()).host(host).build());
		}
		WifiBulkBrowseRequest<IptimeBrowseRequest> bulkOnConnectRequest =
				IptimeBulkBrowseRequest.of(requests);

		// when
		List<ClientResponse<OnConnectUserInfos>> clientResponses =
				iptimeBrowseClientAsync.queriesAsync(bulkOnConnectRequest);
		ClientResponse<OnConnectUserInfos> failResponse = clientResponses.get(0);

		// then
		assertAll(
				() -> assertThat(clientResponses).hasSize(BULK_COUNT + 1),
				() -> assertThat(failResponse.getResponse()).isEmpty());
	}
}
