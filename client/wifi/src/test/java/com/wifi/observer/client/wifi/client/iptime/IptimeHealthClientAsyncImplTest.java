package com.wifi.observer.client.wifi.client.iptime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import com.wifi.observer.client.wifi.WifiClientConfig;
import com.wifi.observer.client.wifi.dto.http.IptimeWifiHealthClientDto;
import com.wifi.observer.client.wifi.dto.request.common.CommonWifiHealthRequest;
import com.wifi.observer.client.wifi.dto.request.iptime.IptimeBulkHealthRequest;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.http.request.get.HealthClientQuery;
import com.wifi.observer.client.wifi.model.HealthQueryClientModel;
import com.wifi.observer.client.wifi.model.info.HealthQueryInfo;
import com.wifi.observer.client.wifi.support.generator.iptime.IptimeHealthFutureGenerator;
import com.wifi.observer.test.util.CookieResource;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(classes = {WifiClientConfig.class, CookieResource.class})
@TestPropertySource("classpath:application-test.yml")
@DisplayName("비동기 IPTIME 공유기 헬스체크 테스트")
class IptimeHealthClientAsyncImplTest {

	private static final int BULK_COUNT = 10;

	static String host = "host";

	@InjectMocks @Autowired IptimeHealthFutureGenerator iptimeHealthFutureGenerator;
	@MockBean HealthClientQuery healthClientQuery;

	@Autowired IptimeHealthClientAsyncImpl iptimeHealthClientAsync;

	@AfterEach
	void clean() {
		MDC.clear();
	}

	@Test
	@DisplayName("벌크 헬스체크 성공 테스트")
	void queryBulkHealthTest() {
		// given
		List<CommonWifiHealthRequest> requests = new ArrayList<>();
		for (int i = 0; i < BULK_COUNT; i++) {
			requests.add(CommonWifiHealthRequest.builder().host(host).build());
		}

		IptimeBulkHealthRequest bulkHealthRequest = new IptimeBulkHealthRequest(requests);

		when(healthClientQuery.query(any(IptimeWifiHealthClientDto.class)))
				.thenReturn(
						HealthQueryClientModel.builder()
								.statusInfo(HealthQueryInfo.builder().info(OK).build())
								.build());

		// when
		List<ClientResponse<HttpStatus>> responses =
				iptimeHealthClientAsync.queriesAsync(bulkHealthRequest);

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

		IptimeBulkHealthRequest bulkHealthRequest = new IptimeBulkHealthRequest(requests);

		when(healthClientQuery.query(argThat(dto -> dto.getHost().equals(host))))
				.thenReturn(
						HealthQueryClientModel.builder()
								.statusInfo(HealthQueryInfo.builder().info(OK).build())
								.build());

		// when
		List<ClientResponse<HttpStatus>> responses =
				iptimeHealthClientAsync.queriesAsync(bulkHealthRequest);

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
