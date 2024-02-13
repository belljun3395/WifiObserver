package com.observer.common.config.cors;

public interface CorsConfigurationSourceProperties {

	String getPathPattern();

	String getOriginPattern();

	String getAllowedMethods();

	String getAllowedHeaders();

	String getExposedHeaders();

	Boolean getAllowCredentials();

	Long getMaxAge();
}
