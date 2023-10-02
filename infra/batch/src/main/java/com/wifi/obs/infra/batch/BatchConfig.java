package com.wifi.obs.infra.batch;

import com.wifi.obs.infra.slack.SlackConfig;
import com.wifi.observer.client.wifi.WifiClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

// todo error handling job
@Configuration
@ComponentScan(basePackages = BatchConfig.BASE_PACKAGE)
@RequiredArgsConstructor
@EnableAutoConfiguration(exclude = {BatchAutoConfiguration.class})
@Import(value = {SlackConfig.class, WifiClientConfig.class})
public class BatchConfig {

	public static final String BASE_PACKAGE = "com.wifi.obs.infra.batch";
	public static final String SERVICE_NAME = "wifiobs";
	public static final String MODULE_NAME = "batch";
	public static final String BEAN_NAME_PREFIX = SERVICE_NAME + MODULE_NAME;
	public static final String PROPERTY_PREFIX = SERVICE_NAME + "." + MODULE_NAME;
}
