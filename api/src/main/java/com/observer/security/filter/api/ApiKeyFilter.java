package com.observer.security.filter.api;

import com.observer.security.exception.ApiKeyInvalidException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Slf4j
public class ApiKeyFilter extends AbstractPreAuthenticatedProcessingFilter {

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		return resolveApiKey(request);
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return resolveApiKey(request);
	}

	private String resolveApiKey(HttpServletRequest request) {
		String apiKeyValue = request.getHeader("WoKey");
		if (Objects.isNull(apiKeyValue)) {
			ApiKeyInvalidException exception =
					getAccessTokenInvalidException("Api key header is missing");
			throw exception;
		}
		return apiKeyValue;
	}

	private ApiKeyInvalidException getAccessTokenInvalidException(String message) {
		ApiKeyInvalidException exception = new ApiKeyInvalidException(message);
		log.warn(exception.getMessage());
		return exception;
	}
}
