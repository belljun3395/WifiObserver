package com.wifi.observer.test.util;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@TestComponent
@TestPropertySource("classpath:application-test.yml")
public class DocumentResource {
	static String AUTH_RESOURCE = "/html/iptime/iptimeAuth.html";
	static String ONCONNECT_RESOURCE = "/html/iptime/iptimeOnConnect.html";
	static Document authSource;
	static Document onConnectSource;

	public Document getAuthDocument() {
		if (authSource == null) {
			try {
				ClassPathResource authResource = new ClassPathResource(AUTH_RESOURCE);
				authSource = Jsoup.parse(authResource.getFile());
				log.info("인증 html 준비 완료");
				return authSource;
			} catch (IOException e) {
				log.error("인증 html 준비 실패");
			}
		}
		return authSource;
	}

	public Document getOnConnectDocument() {
		if (onConnectSource == null) {
			try {
				ClassPathResource onConnectResource = new ClassPathResource(ONCONNECT_RESOURCE);
				onConnectSource = Jsoup.parse(onConnectResource.getFile());
				log.info("연결 html 준비 완료");
				return onConnectSource;
			} catch (IOException e) {
				log.error("연결 html 준비 실패");
			}
		}
		return onConnectSource;
	}
}
