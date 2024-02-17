package com.observer.client.router.util;

import com.observer.client.router.http.dto.http.RouterConnectBody;
import com.observer.client.router.util.resolver.string.StringResolver;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CookieResolver {

	private final StringResolver resolver;

	public CookieResolver(@Qualifier("cookieNamePatternResolverDecorator") StringResolver resolver) {
		this.resolver = resolver;
	}

	public String resolve(RouterConnectBody<Document> source) {
		return resolver.resolve(source.toString());
	}
}
