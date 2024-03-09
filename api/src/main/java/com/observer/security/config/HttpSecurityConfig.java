package com.observer.security.config;

import com.observer.common.config.cors.CorsConfigurationSourceProperties;
import com.observer.security.authentication.api.ApiKeyAuthProvider;
import com.observer.security.filter.api.ApiKeyFilter;
import com.observer.security.filter.exception.ApiKeyExceptionHandlerFilter;
import com.observer.security.handler.DelegatedAccessDeniedHandler;
import com.observer.security.handler.DelegatedAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class HttpSecurityConfig {

	private final DelegatedAuthenticationEntryPoint authenticationEntryPoint;
	private final DelegatedAccessDeniedHandler accessDeniedHandler;
	private final CorsConfigurationSourceProperties securityCorsConfigurationSourceProperties;
	private final ApiKeyAuthProvider apiKeyAuthProvider;

	@Bean
	@Profile("!prod")
	public SecurityFilterChain localSecurityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.formLogin().disable();
		http.httpBasic().disable();
		http.cors().configurationSource(corsConfigurationSource());

		http.authorizeRequests().antMatchers("/api/v1/**").authenticated().anyRequest().denyAll();

		http.addFilterBefore(
				getTokenInvalidExceptionHandlerFilter(), AbstractPreAuthenticatedProcessingFilter.class);
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
		http.cors().configurationSource(corsConfigurationSource());

		http.authorizeRequests().antMatchers("/api/v1/**").authenticated().anyRequest().denyAll();

		http.addFilterBefore(
				getTokenInvalidExceptionHandlerFilter(), AbstractPreAuthenticatedProcessingFilter.class);
		http.addFilterAt(
				generateAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class);

		http.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler);

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		return http.build();
	}

	public ApiKeyFilter generateAuthenticationFilter() {
		ApiKeyFilter filter = new ApiKeyFilter();
		filter.setAuthenticationManager(new ProviderManager(apiKeyAuthProvider));
		return filter;
	}

	public OncePerRequestFilter getTokenInvalidExceptionHandlerFilter() {
		return new ApiKeyExceptionHandlerFilter();
	}

	@Bean
	@Profile("!prod")
	public WebSecurityCustomizer localWebSecurityFilterIgnoreCustomizer() {
		return web ->
				web.ignoring()
						.antMatchers(
								HttpMethod.GET,
								"/actuator/health",
								"/error",
								"/swagger-ui/*",
								"/swagger-resources/**",
								"/v3/api-docs/**",
								"/openapi3.yaml",
								"/reports/**",
								"/api/v1/members/check")
						.antMatchers(HttpMethod.POST, "/api/v1/members", "/api/v1/members/login")
						.antMatchers(HttpMethod.PUT, "/api/v1/members/key");
	}

	@Bean
	@Profile("prod")
	public WebSecurityCustomizer prodWebSecurityFilterIgnoreCustomizer() {
		return web ->
				web.ignoring()
						.antMatchers(
								HttpMethod.GET,
								"/actuator/health",
								"/error",
								"/swagger-ui/*",
								"/swagger-resources/**",
								"/v3/api-docs/**",
								"/openapi3.yaml",
								"/reports/**",
								"/api/v1/members/check")
						.antMatchers(HttpMethod.POST, "/api/v1/members", "/api/v1/members/login")
						.antMatchers(HttpMethod.PUT, "/api/v1/members/key");
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.addAllowedOriginPattern(
				securityCorsConfigurationSourceProperties.getOriginPattern());
		configuration.addAllowedHeader(securityCorsConfigurationSourceProperties.getAllowedHeaders());
		configuration.addAllowedMethod(securityCorsConfigurationSourceProperties.getAllowedMethods());
		configuration.setAllowCredentials(
				securityCorsConfigurationSourceProperties.getAllowCredentials());

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(
				securityCorsConfigurationSourceProperties.getPathPattern(), configuration);
		return source;
	}
}
