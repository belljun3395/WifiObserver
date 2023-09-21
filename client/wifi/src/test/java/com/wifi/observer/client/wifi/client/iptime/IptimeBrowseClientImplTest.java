package com.wifi.observer.client.wifi.client.iptime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.wifi.observer.client.wifi.WifiClientConfig;
import com.wifi.observer.client.wifi.dto.http.IptimeWifiBrowseClientDto;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBrowseRequest;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBulkBrowseRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.dto.response.OnConnectUserInfos;
import com.wifi.observer.client.wifi.http.request.get.BrowseClientQuery;
import com.wifi.observer.client.wifi.model.BrowseQueryClientModel;
import com.wifi.observer.client.wifi.model.info.BrowseQueryInfo;
import com.wifi.observer.client.wifi.support.converter.iptime.IptimeBrowseConverter;
import com.wifi.observer.client.wifi.support.generator.iptime.IptimeBrowseClientHeaderGenerator;
import com.wifi.observer.client.wifi.support.jsoup.ClientDocument;
import com.wifi.observer.test.util.CookieResource;
import com.wifi.observer.test.util.DocumentResource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@DisplayName("동기 IPTIME 공유기 조회 테스트")
class IptimeBrowseClientImplTest {

	private static final int BULK_COUNT = 10;

	static String host = "host";

	@InjectMocks @Autowired IptimeBrowseClientImpl iptimeBrowseClient;
	@MockBean BrowseClientQuery browseClientQuery;

	@Autowired CookieResource cookieResource;
	@Autowired DocumentResource documentResource;

	@Autowired IptimeBrowseConverter iptimeBrowseConverter;
	@Autowired IptimeBrowseClientHeaderGenerator iptimeBrowseClientHeaderGenerator;

	@AfterEach
	void clean() {
		MDC.clear();
	}

	@Test
	@DisplayName("조회 성공 테스트")
	void browseIptimeOnConnectUsersTest() {
		// given
		String cookie = cookieResource.getDefaultCookie();
		IptimeBrowseRequest request = IptimeBrowseRequest.builder().authInfo(cookie).host(host).build();

		when(browseClientQuery.query(any(IptimeWifiBrowseClientDto.class)))
				.thenReturn(
						BrowseQueryClientModel.builder()
								.usersInfo(
										BrowseQueryInfo.builder()
												.info(ClientDocument.of(documentResource.getOnConnectDocument()))
												.build())
								.host(host)
								.build());

		// when
		ClientResponse<OnConnectUserInfos> response = iptimeBrowseClient.query(request);
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

		when(browseClientQuery.query(any(IptimeWifiBrowseClientDto.class)))
				.thenReturn(BrowseQueryClientModel.fail(host));

		// when
		ClientResponse<OnConnectUserInfos> response = iptimeBrowseClient.query(request);

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
					IptimeBrowseRequest.builder()
							.authInfo(cookieResource.getDefaultCookie())
							.host(host)
							.build());
		}
		IptimeBulkBrowseRequest bulkOnConnectRequest = IptimeBulkBrowseRequest.of(requests);

		when(browseClientQuery.query(any(IptimeWifiBrowseClientDto.class)))
				.thenReturn(
						BrowseQueryClientModel.builder()
								.usersInfo(
										BrowseQueryInfo.builder()
												.info(ClientDocument.of(documentResource.getOnConnectDocument()))
												.build())
								.build());

		// when
		List<ClientResponse<OnConnectUserInfos>> responses =
				iptimeBrowseClient.queries(bulkOnConnectRequest);

		// then
		responses.stream()
				.map(ClientResponse::getResponse)
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
		IptimeBrowseRequest successRequest =
				IptimeBrowseRequest.builder()
						.authInfo(cookieResource.getDefaultCookie())
						.host(host)
						.build();
		for (int i = 0; i < BULK_COUNT; i++) {
			requests.add(successRequest);
		}
		IptimeBulkBrowseRequest bulkOnConnectRequest = IptimeBulkBrowseRequest.of(requests);

		Map<String, String> successHeaders =
				iptimeBrowseClientHeaderGenerator.execute(successRequest.getHost());
		IptimeWifiBrowseClientDto successDto =
				iptimeBrowseConverter.to(successRequest, successHeaders, successRequest.getAuthInfo());

		Map<String, String> failHeaders =
				iptimeBrowseClientHeaderGenerator.execute(failRequest.getHost());
		IptimeWifiBrowseClientDto failDto =
				iptimeBrowseConverter.to(failRequest, failHeaders, failRequest.getAuthInfo());

		when(browseClientQuery.query(successDto))
				.thenReturn(
						BrowseQueryClientModel.builder()
								.usersInfo(
										BrowseQueryInfo.builder()
												.info(ClientDocument.of(documentResource.getOnConnectDocument()))
												.build())
								.host(successRequest.getHost())
								.build());

		when(browseClientQuery.query(failDto))
				.thenReturn(BrowseQueryClientModel.fail(failDto.getHost()));

		// when
		List<ClientResponse<OnConnectUserInfos>> clientResponses =
				iptimeBrowseClient.queries(bulkOnConnectRequest);
		ClientResponse<OnConnectUserInfos> failResponse = clientResponses.get(0);

		// then
		assertAll(
				() -> assertThat(clientResponses).hasSize(BULK_COUNT + 1),
				() -> assertThat(failResponse.getResponse()).isEmpty());
	}
}
