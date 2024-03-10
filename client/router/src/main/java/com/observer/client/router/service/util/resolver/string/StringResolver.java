package com.observer.client.router.service.util.resolver.string;

import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface StringResolver {
	String resolve(String source);

	/** 패턴을 통해 원하는 값을 파싱하기 위한 메서드 */
	default String findBracketTextByPattern(Pattern pattern, String text) {
		Matcher matcher = pattern.matcher(text);

		String pureText = text;
		String findText = "";

		while (matcher.find()) {
			int startIndex = matcher.start();
			int endIndex = matcher.end();

			findText = pureText.substring(startIndex, endIndex);
			pureText = pureText.replace(findText, "");
			matcher = pattern.matcher(pureText);
		}

		if (findText.equals("")) {
			throw new NoSuchElementException("[" + findText + "] is not found.");
		}

		return findText;
	}
}
