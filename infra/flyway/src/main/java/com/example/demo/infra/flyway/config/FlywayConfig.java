package com.example.demo.infra.flyway.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = FlywayConfig.BASE_PACKAGE)
public class FlywayConfig {
	public static final String BASE_PACKAGE = "com.example.demo.infra.flyway";
}
