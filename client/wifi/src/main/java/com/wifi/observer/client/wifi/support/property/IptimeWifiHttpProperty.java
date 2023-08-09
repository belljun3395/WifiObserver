package com.wifi.observer.client.wifi.support.property;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@ToString
@EqualsAndHashCode
@Component
@PropertySource("classpath:application-client-wifi.yml")
public class IptimeWifiHttpProperty {

	@Value("${iptime.http.useragent}")
	private String userAgent;

	@Value("${iptime.http.accept.nodeValue}")
	private String accept;

	@Value("${iptime.http.accept.encoding}")
	private String acceptEncoding;

	@Value("${iptime.http.accept.language}")
	private String acceptLanguage;

	@Value("${iptime.http.cache.control}")
	private String cacheControl;

	@Value("${iptime.http.content.length}")
	private String contentLength;

	@Value("${iptime.http.connection}")
	private String connection;

	@Value("${iptime.http.content.type}")
	private String contentType;

	@Value("${iptime.http.upgrade.insecure.request}")
	private String upgradeInsecureRequest;

	@Value("${iptime.http.login.session}")
	private String loginSession;

	@Value("${iptime.http.login.handler}")
	private String loginHandler;

	@Value("${iptime.http.login.query}")
	private String loginQuery;

	@Value("${iptime.http.login.timepro.query}")
	private String loginTimeproQuery;

	@Value("${iptime.http.init.status}")
	private String initStatus;

	@Value("${iptime.http.captcha.on}")
	private String captchaOn;
}
