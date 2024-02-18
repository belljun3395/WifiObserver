package com.observer.web.config;

import com.observer.common.config.cors.CorsConfigurationSourceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class CorsWebConfigurer implements WebMvcConfigurer {

	private final CorsConfigurationSourceProperties webCorsConfigurationSourceProperties;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
				.addMapping(webCorsConfigurationSourceProperties.getPathPattern())
				.allowedOriginPatterns(webCorsConfigurationSourceProperties.getOriginPattern())
				.allowedMethods(webCorsConfigurationSourceProperties.getAllowedMethods())
				.allowedHeaders(webCorsConfigurationSourceProperties.getAllowedHeaders())
				.allowCredentials(webCorsConfigurationSourceProperties.getAllowCredentials())
				.exposedHeaders(webCorsConfigurationSourceProperties.getExposedHeaders())
				.maxAge(webCorsConfigurationSourceProperties.getMaxAge());
	}
}
