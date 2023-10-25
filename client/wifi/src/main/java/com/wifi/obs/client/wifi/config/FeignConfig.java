package com.wifi.obs.client.wifi.config;

import static com.wifi.obs.client.wifi.WifiClientConfig.BEAN_NAME_PREFIX;

import com.wifi.obs.client.wifi.WifiClientConfig;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = WifiClientConfig.BASE_PACKAGE)
@ImportAutoConfiguration({FeignAutoConfiguration.class, HttpClientConfiguration.class})
public class FeignConfig {

	@Bean(name = BEAN_NAME_PREFIX + "HttpMessageConverters")
	HttpMessageConverters httpMessageConverters() {
		return new HttpMessageConverters();
	}
}
