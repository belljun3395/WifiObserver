package com.observer.client.router.config;

import static com.observer.client.router.config.ClientRouterConfig.BEAN_NAME_PREFIX;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = ClientRouterConfig.BASE_PACKAGE)
@ImportAutoConfiguration({FeignAutoConfiguration.class, HttpClientConfiguration.class})
public class FeignConfig {

	@Bean(name = BEAN_NAME_PREFIX + "HttpMessageConverters")
	HttpMessageConverters httpMessageConverters() {
		return new HttpMessageConverters();
	}
}
