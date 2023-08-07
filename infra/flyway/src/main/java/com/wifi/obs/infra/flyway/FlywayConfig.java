package com.wifi.obs.infra.flyway;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = FlywayConfig.BASE_PACKAGE)
public class FlywayConfig {
	public static final String BASE_PACKAGE = "com.wifi.obs.infra.flyway";
}
