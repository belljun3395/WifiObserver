package com.wifi.obs.infra.batch.config;

import static com.wifi.obs.infra.batch.BatchConfig.BEAN_NAME_PREFIX;

import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchSupportConfig {

	public static final String PROPERTY_BEAN_NAME = BEAN_NAME_PREFIX + "Properties";

	@Bean(name = PROPERTY_BEAN_NAME)
	@ConfigurationProperties(prefix = "wifiobs.batch")
	public BatchProperties batchProperties() {
		return new BatchProperties();
	}
}
