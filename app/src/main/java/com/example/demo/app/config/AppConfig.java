package com.example.demo.app.config;

import com.example.demo.data.mysql.config.MysqlConfig;
import com.example.demo.infra.flyway.config.FlywayConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = AppConfig.BASE_PACKAGE)
@Import(value = {MysqlConfig.class, FlywayConfig.class})
public class AppConfig {

	public static final String BASE_PACKAGE = "com.example.demo.app";
}
