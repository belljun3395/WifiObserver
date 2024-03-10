package com.observer.client.router.service.util.resolver.users;

import com.observer.client.router.http.dto.http.iptime.IptimeRouterConnectBody;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jsoup.nodes.Element;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
public class RouterUsersSupport {

	private IptimeRouterConnectBody body;

	public static RouterUsersSupport of(IptimeRouterConnectBody body) {
		return RouterUsersSupport.builder().body(body).build();
	}

	RouterUserSource getSource() {
		return RouterUserSource.builder().userSource(body.getBody()).build();
	}

	@Getter
	@ToString
	@EqualsAndHashCode
	@AllArgsConstructor
	@Builder(toBuilder = true)
	static class RouterUserSource {
		private Element userSource;
	}
}
