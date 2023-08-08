package com.wifi.obs.app.config;

import com.wifi.obs.data.mysql.MysqlConfig;
import com.wifi.obs.infra.batch.BatchConfig;
import com.wifi.obs.infra.flyway.FlywayConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = AppConfig.BASE_PACKAGE)
@Import(value = {MysqlConfig.class, FlywayConfig.class, BatchConfig.class})
public class AppConfig {

	public static final String BASE_PACKAGE = "com.wifi.obs.app";
	public static final String SERVICE_NAME = "wifiobs";
	public static final String MODULE_NAME = "app";
	public static final String BEAN_NAME_PREFIX = SERVICE_NAME + MODULE_NAME;
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;
}
