package com.observer.client.router.service.util.resolver.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/** IPTIME 공유기에서 접속 중인 사용자 정보를 추출하는 기능을 구현한 데코레이터 클래스 */
@Component
public class IptimeRouterUsersOnConnectFilterDecorator extends RouterUsersResolverDecorator {
	private static final String TBODY = "tbody";
	private static final String INPUT = "input";

	public IptimeRouterUsersOnConnectFilterDecorator(
			@Qualifier(value = "iptimeRouterUsersResolver") RouterUsersResolver routerUsersResolver) {
		super(routerUsersResolver);
	}

	@Override
	public List<String> resolve(RouterUsersSupport source) {
		List<String> allUsersProperties = super.resolve(source);

		List<String> onConnectUserProperties =
				getOnConnectUserProperties(source.getSource().getUserSource());

		return filterOnConnectUsers(allUsersProperties, onConnectUserProperties);
	}

	private List<String> getOnConnectUserProperties(Element userSource) {
		Elements tbody = userSource.select(TBODY);

		return tbody.stream()
				.map(e -> e.select(INPUT))
				.flatMap(Collection::stream)
				.map(e -> e.val().toLowerCase())
				.collect(Collectors.toList());
	}

	private List<String> filterOnConnectUsers(List<String> source, List<String> target) {
		List<String> result = new ArrayList<>();
		for (int i = 0; i < target.size(); i = i + 3) {
			if (isOnConnectUser(source, target, i)) {
				result.add(target.get(i).toUpperCase());
			}
		}
		return result;
	}

	private boolean isOnConnectUser(List<String> source, List<String> target, int i) {
		return source.contains(target.get(i));
	}
}
