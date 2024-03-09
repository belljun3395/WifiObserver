package com.observer.security.authentication.api;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthProvider implements AuthenticationProvider {

	private final ApiKeyUserDetailsService apiKeyUserDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException, AccessDeniedException {

		String apiKey =
				Optional.ofNullable(authentication.getPrincipal())
						.map(String.class::cast)
						.orElseThrow(() -> new IllegalArgumentException("apiKey is missing"));

		UserDetails userDetails = apiKeyUserDetailsService.loadUserByUsername(apiKey);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		if (authentication instanceof PreAuthenticatedAuthenticationToken) {
			return new PreAuthenticatedAuthenticationToken(
					userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
