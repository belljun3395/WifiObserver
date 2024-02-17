package com.observer.client.router.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {ClientRouterConfig.BASE_PACKAGE})
public class ClientRouterConfig {
	public static final String BASE_PACKAGE = "com.observer.client.router";
	public static final String SERVICE_NAME = "observer";
	public static final String MODULE_NAME = "router";
	public static final String BEAN_NAME_PREFIX = "clRouter";
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;

	@Bean(name = BEAN_NAME_PREFIX + "ObjectMapper")
	ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
