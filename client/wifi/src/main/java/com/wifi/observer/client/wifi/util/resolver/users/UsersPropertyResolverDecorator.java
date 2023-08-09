package com.wifi.observer.client.wifi.util.resolver.users;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;

/** 사용자 정보를 추출하는 기능을 데코레이터 패턴으로 구현한 클래스 */
@RequiredArgsConstructor
public class UsersPropertyResolverDecorator implements UsersPropertyResolver {

	private final UsersPropertyResolver usersPropertyResolver;

	/**
	 * 추출된 사용자 정보를 데코레이트 한다.
	 *
	 * @param source 사용자 정보가 포함된 HTML
	 * @return 꾸며진 사용자 정보
	 */
	@Override
	public List<String> resolve(Optional<Document> source) {
		return usersPropertyResolver.resolve(source);
	}
}
