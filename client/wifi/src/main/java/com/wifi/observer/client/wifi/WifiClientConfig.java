package com.wifi.observer.client.wifi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {WifiClientConfig.BASE_PACKAGE})
public class WifiClientConfig {
	public static final String BASE_PACKAGE = "com.wifi.observer.client.wifi";
	public static final String SERVICE_NAME = "wifiobs";
	public static final String MODULE_NAME = "wifi";
	public static final String BEAN_NAME_PREFIX = SERVICE_NAME + MODULE_NAME;
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;

	@Bean(name = BEAN_NAME_PREFIX + "ObjectMapper")
	ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
