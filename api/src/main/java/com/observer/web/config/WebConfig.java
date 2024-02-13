package com.observer.web.config;

import com.observer.common.config.cors.CorsConfigurationSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = WebConfig.BASE_PACKAGE)
public class WebConfig {
	public static final String BASE_PACKAGE = "com.observer.web";
	public static final String SERVICE_NAME = "observer";
	public static final String MODULE_NAME = "web";
	public static final String BEAN_NAME_PREFIX = "Web";
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;

	@Bean
	public CorsConfigurationSourceProperties webCorsConfigurationSourceProperties(
			CorsConfigurationSourceProperties corsConfigurationSourceProperties) {
		return corsConfigurationSourceProperties;
	}
}
