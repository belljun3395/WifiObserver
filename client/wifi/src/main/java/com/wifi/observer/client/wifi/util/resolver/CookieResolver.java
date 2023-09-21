package com.wifi.observer.client.wifi.util.resolver;

import com.wifi.observer.client.wifi.model.info.AuthCommandInfo;
import com.wifi.observer.client.wifi.util.resolver.string.StringResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CookieResolver {

	private final StringResolver resolver;

	public CookieResolver(@Qualifier("cookieNamePatternResolverDecorator") StringResolver resolver) {
		this.resolver = resolver;
	}

	public String resolve(AuthCommandInfo source) {
		return resolver.resolve(source.getInfo().toString());
	}
}
