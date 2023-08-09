package com.wifi.observer.client.wifi.client.iptime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import com.wifi.observer.client.wifi.WifiClientConfig;
import com.wifi.observer.client.wifi.dto.request.WifiBulkHealthRequest;
import com.wifi.observer.client.wifi.dto.request.common.CommonWifiHealthRequest;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBulkHealthRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.dto.response.common.CommonHealthStatusResponse;
import com.wifi.observer.client.wifi.exception.WifiURISyntaxException;
import com.wifi.observer.test.util.CookieResource;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(classes = {WifiClientConfig.class, CookieResource.class})
@TestPropertySource("classpath:application-test.yml")
@DisplayName("동기 IPTIME 공유기 헬스체 테스트")
class IptimeHealthClientImplTest {

	private static final int BULK_COUNT = 10;

	@Value("${test.host}")
	String host;

	@Value("${test.hosts}")
	String[] hosts;

	@Autowired IptimeHealthClientImpl iptimeHealthClient;

	@AfterEach
	void clean() {
		MDC.clear();
	}

	@Test
	@DisplayName("성공 테스트")
	void queryHealthTest() {

		// given
		CommonWifiHealthRequest request = CommonWifiHealthRequest.builder().host(host).build();

		// when
		CommonHealthStatusResponse response = iptimeHealthClient.query(request);

		// then
		assertThat(response.getResponse()).containsSame(OK);
	}

	@Test
	@DisplayName("헬스체크 테스트")
	void queryHealthFailTest() {
		// given
		CommonWifiHealthRequest request = CommonWifiHealthRequest.builder().host("fail").build();

		// when
		CommonHealthStatusResponse response = iptimeHealthClient.query(request);

		// then
		assertThat(response.getResponse()).containsSame(BAD_REQUEST);
	}

	@Test
	@DisplayName("URI 형식 실패 테스트")
	void queryHealthURIFailTest() {
		// given
		CommonWifiHealthRequest request = CommonWifiHealthRequest.builder().host("").build();

		// when

		// then
		Assertions.assertThatThrownBy(() -> iptimeHealthClient.query(request))
				.isInstanceOf(WifiURISyntaxException.class)
				.hasMessageContaining(" : 올바르지 않은 URI 형식입니다.");
	}

	@Test
	@DisplayName("헬스체 실패 테스트")
	void queryHealthRequestFailTest() {
		// given
		CommonWifiHealthRequest request = CommonWifiHealthRequest.builder().host(host + "fail").build();

		// when
		CommonHealthStatusResponse response = iptimeHealthClient.query(request);

		// then
		assertThat(response.getResponse()).containsSame(BAD_REQUEST);
	}

	@Test
	@DisplayName("URL NOT FOUND 실패 테스트")
	void queryHealthURLNOTFOUNDFailTest() {
		// given
		CommonWifiHealthRequest request =
				CommonWifiHealthRequest.builder().host(host + "/fail").build();

		// when
		CommonHealthStatusResponse response = iptimeHealthClient.query(request);

		// then
		assertThat(response.getResponse()).containsSame(BAD_REQUEST);
	}

	@Test
	@DisplayName("벌크 헬스체크 성공 테스트")
	void queryBulkHealthTest() {
		// given
		List<CommonWifiHealthRequest> requests = new ArrayList<>();
		for (int i = 0; i < BULK_COUNT; i++) {
			requests.add(CommonWifiHealthRequest.builder().host(host).build());
		}

		WifiBulkHealthRequest bulkHealthRequest = new IptimeBulkHealthRequest(requests);

		// when
		List<ClientResponse<HttpStatus>> responses = iptimeHealthClient.queries(bulkHealthRequest);

		// then
		responses.forEach(response -> assertThat(response.getResponse()).containsSame(OK));
	}

	@Test
	@DisplayName("벌크 헬스체크 실패 포함 테스트")
	void queryIncludeFailHealthTest() {
		// given
		List<CommonWifiHealthRequest> requests = new ArrayList<>();
		for (int i = 0; i < BULK_COUNT; i++) {
			requests.add(CommonWifiHealthRequest.builder().host(host).build());
		}

		WifiBulkHealthRequest bulkHealthRequest = new IptimeBulkHealthRequest(requests);

		// when
		List<ClientResponse<HttpStatus>> responses = iptimeHealthClient.queries(bulkHealthRequest);

		// then
		for (int i = 0; i < responses.size(); i++) {
			if (i < BULK_COUNT) {
				assertThat(responses.get(i).getResponse()).containsSame(OK);
			} else {
				assertThat(responses.get(i).getResponse()).containsSame(BAD_REQUEST);
			}
		}
	}
}
