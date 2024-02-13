package com.observer.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = ApiConfig.BASE_PACKAGE)
public class ApiConfig {

	public static final String BASE_PACKAGE = "com.observer";
	public static final String SERVICE_NAME = "observer";
	public static final String MODULE_NAME = "api";
	public static final String BEAN_NAME_PREFIX = "Api";
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;
}
