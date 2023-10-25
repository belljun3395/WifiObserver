package com.wifi.obs.client.wifi.client.iptime.connectTag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import com.wifi.obs.client.wifi.WifiClientConfig;
import com.wifi.obs.client.wifi.client.iptime.IptimeHealthClient;
import com.wifi.obs.client.wifi.dto.request.WifiBulkHealthRequest;
import com.wifi.obs.client.wifi.dto.request.WifiHostRequest;
import com.wifi.obs.client.wifi.dto.request.iptime.IptimeBulkHealthRequest;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.exception.WifiURISyntaxException;
import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import com.wifi.obs.test.util.CookieResource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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
@DisplayName("동기 IPTIME 공유기 헬스체크 연결 테스트")
@Tag("connect")
class IptimeHealthClientConnectTest {

	private static final int BULK_COUNT = 10;

	@Value("${test.host}")
	String host;

	@Value("${test.hosts}")
	String[] hosts;

	@Autowired IptimeHealthClient iptimeHealthClient;

	@BeforeEach
	void setUp() {
		MDC.put("request_id", UUID.randomUUID().toString());
	}

	@AfterEach
	void clean() {
		MDC.clear();
	}

	@Test
	@DisplayName("성공 테스트")
	void queryHealthTest() {

		// given
		WifiHostRequest request = WifiHostRequest.builder().host(host).build();

		// when
		ClientResponse<HttpStatusResponse> response = iptimeHealthClient.query(request);

		// then
		assertThat(response.getResponse()).contains(HttpStatusResponse.of(OK));
	}

	@Test
	@DisplayName("실패 테스트")
	void queryHealthFailTest() {
		// given
		WifiHostRequest request = WifiHostRequest.builder().host("fail").build();

		// when
		ClientResponse<HttpStatusResponse> response = iptimeHealthClient.query(request);

		// then
		assertThat(response.getResponse()).contains(HttpStatusResponse.of(BAD_REQUEST));
	}

	@Test
	@DisplayName("URI 형식 실패 테스트")
	void queryHealthURIFailTest() {
		// given
		WifiHostRequest request = WifiHostRequest.builder().host("").build();

		// when

		// then
		Assertions.assertThatThrownBy(() -> iptimeHealthClient.query(request))
				.isInstanceOf(WifiURISyntaxException.class)
				.hasMessageContaining("올바르지 않은 URI 형식입니다");
	}

	@Test
	@DisplayName("host 실패 테스트")
	void queryHealthRequestFailTest() {
		// given
		WifiHostRequest request = WifiHostRequest.builder().host(host + "fail").build();

		// when
		ClientResponse<HttpStatusResponse> response = iptimeHealthClient.query(request);

		// then
		assertThat(response.getResponse()).contains(HttpStatusResponse.of(BAD_REQUEST));
	}

	@Test
	@DisplayName("URL NOT FOUND 실패 테스트")
	void queryHealthURLNOTFOUNDFailTest() {
		// given
		WifiHostRequest request = WifiHostRequest.builder().host(host + "/fail").build();

		// when
		ClientResponse<HttpStatusResponse> response = iptimeHealthClient.query(request);

		// then
		assertThat(response.getResponse()).contains(HttpStatusResponse.of(BAD_REQUEST));
	}

	@Test
	@DisplayName("벌크 헬스체크 성공 테스트")
	void queryBulkHealthTest() {
		// given
		List<WifiHostRequest> requests = new ArrayList<>();
		for (int i = 0; i < BULK_COUNT; i++) {
			requests.add(WifiHostRequest.builder().host(host).build());
		}

		WifiBulkHealthRequest<WifiHostRequest> bulkHealthRequest =
				new IptimeBulkHealthRequest(requests);

		// when
		List<ClientResponse<HttpStatusResponse>> responses =
				iptimeHealthClient.queries(bulkHealthRequest);

		// then
		responses.forEach(
				response -> assertThat(response.getResponse()).contains(HttpStatusResponse.of(OK)));
	}

	@Test
	@DisplayName("벌크 헬스체크 실패 포함 테스트")
	void queryIncludeFailHealthTest() {
		// given
		List<WifiHostRequest> requests = new ArrayList<>();
		for (int i = 0; i < BULK_COUNT; i++) {
			requests.add(WifiHostRequest.builder().host(host).build());
		}

		WifiBulkHealthRequest<WifiHostRequest> bulkHealthRequest =
				new IptimeBulkHealthRequest(requests);

		// when
		List<ClientResponse<HttpStatusResponse>> responses =
				iptimeHealthClient.queries(bulkHealthRequest);

		// then
		for (int i = 0; i < responses.size(); i++) {
			if (i < BULK_COUNT) {
				assertThat(responses.get(i).getResponse()).contains(HttpStatusResponse.of(OK));
			} else {
				assertThat(responses.get(i).getResponse()).contains(HttpStatusResponse.of(BAD_REQUEST));
			}
		}
	}
}
