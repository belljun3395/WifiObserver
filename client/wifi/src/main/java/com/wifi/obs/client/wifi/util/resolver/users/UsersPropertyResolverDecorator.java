package com.wifi.obs.client.wifi.util.resolver.users;

import com.wifi.obs.client.wifi.model.value.BrowseQueryVO;
import java.util.List;
import lombok.RequiredArgsConstructor;

/** 사용자 정보를 추출하는 기능을 데코레이터 패턴으로 구현한 클래스 */
@RequiredArgsConstructor
public class UsersPropertyResolverDecorator implements UsersPropertyResolver {

	private final UsersPropertyResolver usersPropertyResolver;

	@Override
	public List<String> resolve(BrowseQueryVO source) {
		return usersPropertyResolver.resolve(source);
	}
}
