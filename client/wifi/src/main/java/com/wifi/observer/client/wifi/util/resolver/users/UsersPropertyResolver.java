package com.wifi.observer.client.wifi.util.resolver.users;

import com.wifi.observer.client.wifi.model.info.BrowseQueryInfo;
import java.util.List;

public interface UsersPropertyResolver {

	List<String> resolve(BrowseQueryInfo source);
}
