package com.wifi.observer.client.wifi.client.iptime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.wifi.observer.client.wifi.WifiClientConfig;
import com.wifi.observer.client.wifi.dto.http.IptimeWifiBrowseClientDto;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBrowseRequest;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBulkBrowseRequest;
import com.wifi.observer.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
import com.wifi.observer.client.wifi.http.request.get.BrowseClientQuery;
import com.wifi.observer.client.wifi.support.generator.iptime.IptimeBrowseClientFutureGenerator;
import com.wifi.observer.test.util.CookieResource;
import com.wifi.observer.test.util.DocumentResource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
@DisplayName("비동기 IPTIME 공유기 조회 테스트")
class IptimeBrowseClientAsyncImplTest {

	private static final int BULK_COUNT = 10;

	static String host = "host";
	static String userName = "userName";
	static String password = "password";

	@InjectMocks @Autowired IptimeBrowseClientFutureGenerator iptimeBrowseClientFutureGenerator;
	@MockBean BrowseClientQuery browseClientQuery;

	@Autowired IptimeBrowseClientAsyncImpl iptimeBrowseClientAsync;
	@Autowired CookieResource cookieResource;
	@Autowired DocumentResource documentResource;

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
					IptimeBrowseRequest.builder()
							.authInfo(cookieResource.getDefaultCookie())
							.host(host)
							.build());
		}
		IptimeBulkBrowseRequest bulkOnConnectRequest = IptimeBulkBrowseRequest.of(requests);

		when(browseClientQuery.query(any(IptimeWifiBrowseClientDto.class)))
				.thenReturn(Optional.of(documentResource.getOnConnectDocument()));

		// when
		List<IptimeOnConnectUserInfosResponse> responses =
				iptimeBrowseClientAsync.queriesAsync(bulkOnConnectRequest);

		// then
		responses.stream()
				.map(IptimeOnConnectUserInfosResponse::getResponse)
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
					IptimeBrowseRequest.builder()
							.authInfo(cookieResource.getDefaultCookie())
							.host(host)
							.build());
		}
		IptimeBulkBrowseRequest bulkOnConnectRequest = IptimeBulkBrowseRequest.of(requests);

		when(browseClientQuery.query(
						ArgumentMatchers.argThat(
								dto ->
										dto.getHost()
												.equals(
														host + "/sess-bin/timepro.cgi?tmenu=iframe&smenu=lan_pcinfo_status"))))
				.thenReturn(Optional.of(documentResource.getOnConnectDocument()));

		// when
		List<IptimeOnConnectUserInfosResponse> clientResponses =
				iptimeBrowseClientAsync.queriesAsync(bulkOnConnectRequest);
		IptimeOnConnectUserInfosResponse failResponse = clientResponses.get(0);

		// then
		assertAll(
				() -> assertThat(clientResponses).hasSize(BULK_COUNT + 1),
				() -> assertThat(failResponse.getResponse()).isEmpty());
	}
}
