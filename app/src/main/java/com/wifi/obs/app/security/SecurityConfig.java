package com.wifi.obs.app.security;

import com.wifi.obs.app.security.authentication.token.TokenAuthProvider;
import com.wifi.obs.app.security.filter.token.TokenAuthenticationFilter;
import com.wifi.obs.app.security.handler.DelegatedAccessDeniedHandler;
import com.wifi.obs.app.security.handler.DelegatedAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final DelegatedAuthenticationEntryPoint authenticationEntryPoint;
	private final DelegatedAccessDeniedHandler accessDeniedHandler;
	private final TokenAuthProvider tokenAuthProvider;

	@Bean
	@Profile("!prod")
	public SecurityFilterChain localSecurityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.formLogin().disable();
		http.httpBasic().disable();
		http.authorizeRequests()
				.antMatchers(
						HttpMethod.GET,
						"/swagger-ui/*",
						"/api-docs/*",
						"/openapi3.yaml",
						"/actuator/health",
						"/reports/**",
						"/error")
				.permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/members", "/api/v1/members/tokens")
				.permitAll()
				.antMatchers("/api/v1/**")
				.authenticated()
				.anyRequest()
				.denyAll();

		http.addFilterAt(
				generateAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class);

		http.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler);

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		return http.build();
	}

	@Bean
	@Profile(value = "prod")
	public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.formLogin().disable();
		http.httpBasic().disable();

		http.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/openapi3.yaml", "/actuator/health", "/reports/**", "/error")
				.permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/members", "/api/v1/members/tokens")
				.permitAll()
				.antMatchers("/api/v1/**")
				.authenticated()
				.anyRequest()
				.denyAll();

		http.addFilterAt(
				generateAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class);

		http.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler);

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		return http.build();
	}

	public TokenAuthenticationFilter generateAuthenticationFilter() {
		TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter();
		tokenAuthenticationFilter.setAuthenticationManager(new ProviderManager(tokenAuthProvider));
		return tokenAuthenticationFilter;
	}
}
