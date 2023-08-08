package com.wifi.obs.infra.flyway;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = FlywayConfig.BASE_PACKAGE)
public class FlywayConfig {
	public static final String BASE_PACKAGE = "com.wifi.obs.infra.flyway";
	public static final String SERVICE_NAME = "wifiobs";
	public static final String MODULE_NAME = "flyway";
	public static final String BEAN_NAME_PREFIX = SERVICE_NAME + MODULE_NAME;
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;
}
