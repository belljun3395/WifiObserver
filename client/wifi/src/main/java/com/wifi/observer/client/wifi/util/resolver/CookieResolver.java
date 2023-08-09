package com.wifi.observer.client.wifi.util.resolver;

import com.wifi.observer.client.wifi.util.resolver.string.StringResolver;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CookieResolver {

	private final StringResolver resolver;

	public CookieResolver(@Qualifier("cookieNamePatternResolverDecorator") StringResolver resolver) {
		this.resolver = resolver;
	}

	public String resolve(Document source) {
		return resolver.resolve(source.toString());
	}
}
