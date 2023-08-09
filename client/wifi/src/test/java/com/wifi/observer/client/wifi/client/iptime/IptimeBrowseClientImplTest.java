package com.wifi.observer.client.wifi.client.iptime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wifi.observer.client.wifi.WifiClientConfig;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBrowseRequest;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBulkBrowseRequest;
import com.wifi.observer.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
import com.wifi.observer.test.util.CookieResource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("동기 IPTIME 공유기 조회 테스트")
class IptimeBrowseClientImplTest {

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

	@Autowired IptimeBrowseClientImpl iptimeBrowseClient;
	@Autowired CookieResource cookieResource;

	@AfterEach
	void clean() {
		MDC.clear();
	}

	@Test
	@DisplayName("조회 성공 테스트")
	void browseIptimeOnConnectUsersTest() {
		// given
		String cookie = cookieResource.getCookie();
		IptimeBrowseRequest request = IptimeBrowseRequest.builder().authInfo(cookie).host(host).build();

		// when
		IptimeOnConnectUserInfosResponse response = iptimeBrowseClient.query(request);
		if (response.getResponse().isEmpty()) {
			log.info("response is empty");
		}

		// then
		assertAll(
				() -> assertThat(response.getResponse().get().getUsers()).isNotEmpty(),
				() -> assertThat(response.getHost()).isEqualTo(host));
	}

	@Test
	@DisplayName("조회 실패 테스트")
	void browseIptimeOnConnectUsersFailTest() {
		// given
		String cookie = "wrong cookie";
		String host = "wrong host";
		IptimeBrowseRequest request = IptimeBrowseRequest.builder().authInfo(cookie).host(host).build();

		// when
		IptimeOnConnectUserInfosResponse response = iptimeBrowseClient.query(request);

		// then
		assertThat(response.getResponse()).isEmpty();
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
		IptimeBulkBrowseRequest bulkOnConnectRequest = IptimeBulkBrowseRequest.of(requests);

		// when
		List<IptimeOnConnectUserInfosResponse> responses =
				iptimeBrowseClient.queries(bulkOnConnectRequest);

		// then
		responses.stream()
				.map(IptimeOnConnectUserInfosResponse::getResponse)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.forEach(response -> assertThat(response.getUsers()).isNotEmpty());
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
		IptimeBulkBrowseRequest bulkOnConnectRequest = IptimeBulkBrowseRequest.of(requests);

		// when
		List<IptimeOnConnectUserInfosResponse> clientResponses =
				iptimeBrowseClient.queries(bulkOnConnectRequest);
		IptimeOnConnectUserInfosResponse failResponse = clientResponses.get(0);

		// then
		assertAll(
				() -> assertThat(clientResponses).hasSize(BULK_COUNT + 1),
				() -> assertThat(failResponse.getResponse()).isEmpty());
	}
}
