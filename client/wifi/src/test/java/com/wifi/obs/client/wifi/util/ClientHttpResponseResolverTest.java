package com.wifi.obs.client.wifi.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wifi.obs.client.wifi.http.jsoup.HTMLDocumentResponse;
import com.wifi.obs.client.wifi.model.value.AuthCommandVO;
import com.wifi.obs.client.wifi.model.value.BrowseQueryVO;
import com.wifi.obs.client.wifi.util.resolver.CookieResolver;
import com.wifi.obs.client.wifi.util.resolver.string.CookieNamePatternResolverDecorator;
import com.wifi.obs.client.wifi.util.resolver.string.SetCookiePatternResolver;
import com.wifi.obs.client.wifi.util.resolver.users.IptimeUserPropertyResolver;
import com.wifi.obs.client.wifi.util.resolver.users.IptimeUsersOnConnectFilterDecorator;
import com.wifi.obs.test.util.DocumentResource;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
		classes = {
			IptimeUsersOnConnectFilterDecorator.class,
			IptimeUserPropertyResolver.class,
			CookieResolver.class,
			CookieNamePatternResolverDecorator.class,
			SetCookiePatternResolver.class,
			DocumentResource.class,
		})
@TestPropertySource("classpath:application-test.yml")
class ClientHttpResponseResolverTest {

	static String LOCAL_HOST = "http://localhost";
	static String EXPECT_EXTRACT_COOKIE_VALUE = "2Lr3BFTO4DCFeGQF";
	static String[] EXPECT_EXTRACT_ON_CONNECT_USERS =
			new String[] {
				"D6:98:68:C3:6B:36",
				"D6:38:41:90:BC:63",
				"3C:9C:0F:60:C8:8B",
				"90:32:4B:18:00:1B",
				"BC:D0:74:9F:B0:E3"
			};

	@Autowired IptimeUsersOnConnectFilterDecorator iptimeOnConnectUsersResolver;
	@Autowired CookieResolver cookieResolver;

	@Autowired DocumentResource documentResource;

	@Test
	@DisplayName("IPTIME 쿠키값 파싱 테스트")
	void iptimeCookieParseTest() {
		// given
		Document authSource = documentResource.getAuthDocument();

		// when
		String cookie =
				cookieResolver.resolve(
						AuthCommandVO.builder().info(HTMLDocumentResponse.of(authSource)).build());

		// then
		assertThat(cookie).isEqualTo(EXPECT_EXTRACT_COOKIE_VALUE);
		log.info("쿠키 파싱 완료 : {}", cookie);
	}

	@Test
	@DisplayName("IPTIME 쿠키값 파싱 실패 테스트")
	void iptimeCookieParseFailTest() {
		// given
		Document document = new Document(LOCAL_HOST);

		// when

		// then
		assertThatThrownBy(
						() ->
								cookieResolver.resolve(
										AuthCommandVO.builder().info(HTMLDocumentResponse.of(document)).build()))
				.isInstanceOf(NoSuchElementException.class)
				.hasMessageContaining("값을 찾아낼 수 없습니다.");
	}

	@Test
	@DisplayName("IPTIME 접속자 파싱 테스트")
	void iptimeUserParseTest() {
		// given
		Document onConnectSource = documentResource.getOnConnectDocument();

		// when
		List<String> onConnectUsers =
				iptimeOnConnectUsersResolver.resolve(
						BrowseQueryVO.builder().info(HTMLDocumentResponse.of(onConnectSource)).build());

		// then
		assertThat(onConnectUsers).containsExactlyInAnyOrder(EXPECT_EXTRACT_ON_CONNECT_USERS);
		log.info("접속자 파싱 완료 : {}", onConnectUsers);
	}
}
