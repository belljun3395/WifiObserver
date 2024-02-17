package com.observer.client.router.http.dto.generator.iptime;

import com.observer.client.router.config.properties.IptimeRouterHttpProperties;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IptimeAuthClientBodyGenerator {

	private static final String INIT_STATUS = "init_status";
	private static final String CAPTCHA_ON = "captcha_on";
	private static final String USERNAME = "username";
	private static final String PASSWD = "passwd";
	private final IptimeRouterHttpProperties httpProperties;

	public Map<String, String> execute(String name, String password) {
		Map<String, String> body = new HashMap<>();
		body.put(INIT_STATUS, httpProperties.getInitStatus());
		body.put(CAPTCHA_ON, httpProperties.getCaptchaOn());
		body.put(USERNAME, name);
		body.put(PASSWD, password);
		return body;
	}
}
