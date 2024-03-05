package com.observer.security.authentication.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String apiKey) throws UsernameNotFoundException {
		return ApiKeyUserDetails.builder().apiKeyValue(apiKey).build();
	}
}
