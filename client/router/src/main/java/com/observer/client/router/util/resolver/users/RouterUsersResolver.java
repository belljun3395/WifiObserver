package com.observer.client.router.util.resolver.users;

import com.observer.client.router.http.dto.http.iptime.IptimeRouterConnectBody;
import java.util.List;

public interface RouterUsersResolver {

	List<String> resolve(IptimeRouterConnectBody source);
}
