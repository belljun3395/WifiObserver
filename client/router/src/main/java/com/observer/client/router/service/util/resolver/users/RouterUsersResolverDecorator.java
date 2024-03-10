package com.observer.client.router.service.util.resolver.users;

import java.util.List;
import lombok.RequiredArgsConstructor;

/** 사용자 정보를 추출하는 기능을 데코레이터 패턴으로 구현한 클래스 */
@RequiredArgsConstructor
public class RouterUsersResolverDecorator implements RouterUsersResolver {

	private final RouterUsersResolver routerUsersResolver;

	@Override
	public List<String> resolve(RouterUsersSupport source) {
		return routerUsersResolver.resolve(source);
	}
}
