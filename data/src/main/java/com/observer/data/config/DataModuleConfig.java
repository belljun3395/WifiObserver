package com.observer.data.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = DataModuleConfig.BASE_PACKAGE)
public class DataModuleConfig {
	public static final String BASE_PACKAGE = "com.observer.data";
	public static final String SERVICE_NAME = "observer";
	public static final String MODULE_NAME = "data";
	public static final String BEAN_NAME_PREFIX = "data";
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;
}
