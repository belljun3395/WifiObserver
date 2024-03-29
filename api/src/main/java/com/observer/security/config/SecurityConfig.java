package com.observer.security.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = SecurityConfig.BASE_PACKAGE)
public class SecurityConfig {

	public static final String BASE_PACKAGE = "com.observer.security";
	public static final String SERVICE_NAME = "observer";
	public static final String MODULE_NAME = "security";
	public static final String BEAN_NAME_PREFIX = "security";
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;
}
