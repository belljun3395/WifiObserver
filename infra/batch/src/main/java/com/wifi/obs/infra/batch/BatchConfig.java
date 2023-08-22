package com.wifi.obs.infra.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = BatchConfig.BASE_PACKAGE)
@RequiredArgsConstructor
@EnableAutoConfiguration(exclude = {BatchAutoConfiguration.class})
public class BatchConfig {

	public static final String BASE_PACKAGE = "com.wifi.obs.infra.batch";
	public static final String BEAN_NAME_PREFIX = "wifiobsbatch";
}
