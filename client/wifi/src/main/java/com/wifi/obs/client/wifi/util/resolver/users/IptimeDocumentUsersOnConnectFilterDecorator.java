package com.wifi.obs.client.wifi.util.resolver.users;

import com.wifi.obs.client.wifi.http.HTMLResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/** IPTIME 공유기에서 접속 중인 사용자 정보를 추출하는 기능을 구현한 데코레이터 클래스 */
@Component
public class IptimeDocumentUsersOnConnectFilterDecorator extends UsersPropertyResolverDecorator {
	private static final String TBODY = "tbody";
	private static final String INPUT = "input";

	public IptimeDocumentUsersOnConnectFilterDecorator(
			@Qualifier(value = "iptimeDocumentUserPropertyResolver")
					UsersPropertyResolver usersPropertyResolver) {
		super(usersPropertyResolver);
	}

	@Override
	public List<String> resolve(HTMLResponse source) {
		List<String> allUsersProperties = super.resolve(source);

		List<String> onConnectUserProperties =
				getOnConnectUserProperties(source.getHttpResponse().getCrawlResponse().getBody());

		return filterOnConnectUsers(allUsersProperties, onConnectUserProperties);
	}

	private List<String> getOnConnectUserProperties(Document source) {
		Element body = source.body();
		Elements tbody = body.select(TBODY);

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
