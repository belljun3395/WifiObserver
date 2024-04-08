package com.observer.client.router.service;

import com.observer.client.router.config.ClientRouterConfig;
import com.observer.client.router.exception.ClientException;
import com.observer.client.router.service.iptime.IptimeUsersService;
import com.observer.client.router.support.dto.request.iptime.IptimeUsersServiceRequest;
import com.observer.client.router.support.dto.response.RouterUser;
import com.observer.client.router.support.dto.response.RouterUsersResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@ActiveProfiles(value = {"test", "client-test"})
@SpringBootTest
@ContextConfiguration(classes = {ClientRouterConfig.class})
@TestPropertySource("classpath:application-test.yml")
class RouterUsersServiceTest {

	@Autowired IptimeUsersService iptimeUsersService;

	static final String COOKIE_VALUE = "2Lr3BFTO4DCFeGQF";
	static List<String> EXPECT_EXTRACT_ON_CONNECT_USERS =
			List.of(
					"D6:98:68:C3:6B:36",
					"D6:38:41:90:BC:63",
					"3C:9C:0F:60:C8:8B",
					"90:32:4B:18:00:1B",
					"BC:D0:74:9F:B0:E3");

	@Test
	@DisplayName("Iptime 공유기에서 접속중인 사용자 정보를 획득합니다")
	void iptimeUsersService() throws ClientException {
		// Given
		IptimeUsersServiceRequest request =
				IptimeUsersServiceRequest.builder().authInfo(COOKIE_VALUE).build();

		// When
		RouterUsersResponse response = iptimeUsersService.execute(request);

		// Then
		Assertions.assertEquals(
				EXPECT_EXTRACT_ON_CONNECT_USERS,
				response.getResponse().getUsers().stream()
						.map(RouterUser::getUser)
						.collect(Collectors.toList()));
	}
}
