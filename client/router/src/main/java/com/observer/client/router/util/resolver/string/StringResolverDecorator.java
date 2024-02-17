package com.observer.client.router.util.resolver.string;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StringResolverDecorator implements StringResolver {

	private final StringResolver stringPatternResolver;

	@Override
	public String resolve(String source) {
		return stringPatternResolver.resolve(source);
	}
}
