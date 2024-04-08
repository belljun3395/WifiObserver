package com.observer.client.router.service.util.resolver.string;

import com.observer.client.router.config.ClientRouterConfig;
import java.io.IOException;
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
class StringResolverTest {

	@Autowired SetCookiePatternResolver setCookiePatternResolver;

	@Autowired CookieNamePatternResolverDecorator cookieNamePatternResolverDecorator;

	static final String AUTH_HTML_PATH = "/html/iptime/iptimeAuth.html";

	static final String COOKIE_VALUE = "2Lr3BFTO4DCFeGQF";

	@Test
	@DisplayName("html에서 setCookie 요소를 파싱합니다.")
	void setCookiePatternResolve() throws IOException {
		// Given
		ClassPathResource authResource = new ClassPathResource(AUTH_HTML_PATH);
		Document document = Jsoup.parse(authResource.getFile());

		// When
		String resolve = setCookiePatternResolver.resolve(document.toString());

		// Then
		Assertions.assertTrue(resolve.contains(COOKIE_VALUE));
	}

	@Test
	@DisplayName("html에서 setCookie의 값을 파싱합니다.")
	void cookieNamePatternResolverDecorator() throws IOException {
		// Given
		ClassPathResource authResource = new ClassPathResource(AUTH_HTML_PATH);
		Document document = Jsoup.parse(authResource.getFile());

		// When
		String resolve = cookieNamePatternResolverDecorator.resolve(document.toString());

		// Then
		Assertions.assertEquals(resolve, COOKIE_VALUE);
	}
}
