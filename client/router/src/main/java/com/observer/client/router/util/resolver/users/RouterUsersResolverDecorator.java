package com.observer.client.router.util.resolver.users;

import com.observer.client.router.http.dto.http.iptime.IptimeRouterConnectBody;
import java.util.List;
import lombok.RequiredArgsConstructor;

/** 사용자 정보를 추출하는 기능을 데코레이터 패턴으로 구현한 클래스 */
@RequiredArgsConstructor
public class RouterUsersResolverDecorator implements RouterUsersResolver {

	private final RouterUsersResolver routerUsersResolver;

	@Override
	public List<String> resolve(IptimeRouterConnectBody source) {
		return routerUsersResolver.resolve(source);
	}
}
