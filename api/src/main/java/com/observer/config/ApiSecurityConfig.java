package com.observer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApiSecurityConfig {
	private static final String SECURITY_BEAN_NAME_PREFIX = ApiConfig.BEAN_NAME_PREFIX + "Security";

	private static final String PASSWORD_ENCODER_BEAN_NAME =
			SECURITY_BEAN_NAME_PREFIX + "PasswordEncoder";

	@Bean(name = PASSWORD_ENCODER_BEAN_NAME)
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
