package com.observer.web.controller.config;

import com.observer.security.authentication.api.ApiKeyUserDetails;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@TestComponent
public class TestApiKeyUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return ApiKeyUserDetails.builder().apiKeyValue("31M0RSXT6Y7R29P7WN8ZA7H6706ID14F").build();
	}
}
