package com.wifi.obs.client.wifi.client.iptime.connectTag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import com.wifi.obs.client.wifi.WifiClientConfig;
import com.wifi.obs.client.wifi.client.iptime.IptimeHealthClientAsync;
import com.wifi.obs.client.wifi.dto.request.WifiBulkHealthRequest;
import com.wifi.obs.client.wifi.dto.request.WifiHostRequest;
import com.wifi.obs.client.wifi.dto.request.iptime.IptimeBulkHealthRequest;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.http.HttpStatusResponse;
import com.wifi.obs.test.util.CookieResource;
import java.util.ArrayList;
import java.util.List;
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
@DisplayName("비동기 IPTIME 공유기 헬스체크 연결 테스트")
@Tag("connect")
class IptimeHealthClientAsyncConnectTest {

	private static final int BULK_COUNT = 10;

	@Value("${test.host}")
	String host;

	@Value("${test.hosts}")
	String[] hosts;

	@Autowired IptimeHealthClientAsync iptimeBrowseClientAsync;

	@BeforeEach
	void setUp() {
		MDC.put("request_id", UUID.randomUUID().toString());
	}

	@AfterEach
	void clean() {
		MDC.clear();
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
				iptimeBrowseClientAsync.queriesAsync(bulkHealthRequest);

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
				iptimeBrowseClientAsync.queriesAsync(bulkHealthRequest);

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
