package com.observer.batch.config;

import com.observer.client.router.config.ClientRouterConfig;
import com.observer.data.config.DataModuleConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = BatchConfig.BASE_PACKAGE)
@RequiredArgsConstructor
@EnableAutoConfiguration(exclude = {BatchAutoConfiguration.class})
@Import(value = {DataModuleConfig.class, ClientRouterConfig.class})
public class BatchConfig {

	public static final String BASE_PACKAGE = "com.observer.batch";
	public static final String SERVICE_NAME = "observer";
	public static final String MODULE_NAME = "batch";
	public static final String BEAN_NAME_PREFIX = SERVICE_NAME + MODULE_NAME;
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;
}
