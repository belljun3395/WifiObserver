package com.observer.client.router.util.resolver.string;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

/** Source에서 setCookie에 해당하는 값을 파싱하기 위한 Resolver 클래스 */
@Component
public class SetCookiePatternResolver implements StringResolver {

	private static final Pattern SET_COOKIE_PATTERN = Pattern.compile("setCookie\\('[^()]+'\\)");

	@Override
	public String resolve(String source) {
		return findBracketTextByPattern(SET_COOKIE_PATTERN, source);
	}
}
