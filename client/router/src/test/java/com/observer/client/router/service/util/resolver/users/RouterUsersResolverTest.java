package com.observer.client.router.service.util.resolver.users;

import com.observer.client.router.config.ClientRouterConfig;
import com.observer.client.router.http.dto.http.iptime.IptimeRouterConnectBody;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@ActiveProfiles("client-test")
@SpringBootTest
@ContextConfiguration(classes = {ClientRouterConfig.class})
@TestPropertySource("classpath:application-test.yml")
class RouterUsersResolverTest {

	@Autowired IptimeRouterUsersResolver iptimeRouterUsersResolver;

	@Autowired IptimeRouterUsersOnConnectFilterDecorator iptimeRouterUsersOnConnectFilterDecorator;

	static final String ON_CONNECT_USER_HTML_PATH = "/html/iptime/iptimeOnConnect.html";

	static List<String> EXPECT_EXTRACT_ON_CONNECT_USERS =
			List.of(
					"D6:98:68:C3:6B:36",
					"D6:38:41:90:BC:63",
					"3C:9C:0F:60:C8:8B",
					"90:32:4B:18:00:1B",
					"BC:D0:74:9F:B0:E3");

	@Test
	@DisplayName("Iptime 공유기 접속 원천 정보를 파싱합니다")
	void iptimeRouterUsersResolver() throws IOException {
		// Given
		ClassPathResource authResource = new ClassPathResource(ON_CONNECT_USER_HTML_PATH);
		Document document = Jsoup.parse(authResource.getFile());
		RouterUsersSupport routerUsersSupport =
				RouterUsersSupport.of(IptimeRouterConnectBody.builder().body(document).build());

		// When
		List<String> onConnectRouterSources = iptimeRouterUsersResolver.resolve(routerUsersSupport);

		// When
		int count = 0;
		for (String user : onConnectRouterSources) {
			if (EXPECT_EXTRACT_ON_CONNECT_USERS.contains(user.toUpperCase())) {
				count++;
			}
		}
		Assertions.assertEquals(EXPECT_EXTRACT_ON_CONNECT_USERS.size(), count);
	}

	@Test
	@DisplayName("Iptime 공유기 사용자 접속 정보를 파싱합니다")
	void iptimeRouterUsersOnConnectFilterDecorator() throws IOException {
		// Given
		ClassPathResource authResource = new ClassPathResource(ON_CONNECT_USER_HTML_PATH);
		Document document = Jsoup.parse(authResource.getFile());
		RouterUsersSupport routerUsersSupport =
				RouterUsersSupport.of(IptimeRouterConnectBody.builder().body(document).build());

		// When
		List<String> users = iptimeRouterUsersOnConnectFilterDecorator.resolve(routerUsersSupport);

		// Then
		Assertions.assertEquals(EXPECT_EXTRACT_ON_CONNECT_USERS, users);
	}
}
