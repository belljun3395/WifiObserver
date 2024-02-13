package com.observer.security.config;

import com.observer.common.config.cors.CorsConfigurationSourceProperties;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SecurityCorsConfigurationSourceProperties
		implements CorsConfigurationSourceProperties {

	@Value("${security.cors.path-patterns}")
	private String pathPattern;

	@Value("${security.cors.origin-patterns}")
	private String originPattern;

	@Value("${security.cors.allowed-methods}")
	private String allowedMethods;

	@Value("${security.cors.allowed-headers}")
	private String allowedHeaders;

	@Value("${security.cors.exposed-headers}")
	private String exposedHeaders;

	@Value("${security.cors.allow-credentials}")
	private Boolean allowCredentials;

	@Value("${security.cors.max-age}")
	private Long maxAge;
}
