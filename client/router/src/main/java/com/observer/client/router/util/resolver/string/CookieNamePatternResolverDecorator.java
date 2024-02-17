package com.observer.client.router.util.resolver.string;

import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/** setCookie로 파싱한 값에서 쿠키 값을 파싱하기 위한 데코레이터 클래스 */
@Component
public class CookieNamePatternResolverDecorator extends StringResolverDecorator {

	private static final Pattern EXTRACT_COOKIE_NAME_PATTERN = Pattern.compile("([^()]+)");

	public CookieNamePatternResolverDecorator(
			@Qualifier("setCookiePatternResolver") StringResolver resolver) {
		super(resolver);
	}

	@Override
	public String resolve(String source) {
		String resolved = super.resolve(source);
		return findBracketTextByPattern(EXTRACT_COOKIE_NAME_PATTERN, resolved).replace("\'", "");
	}
}
