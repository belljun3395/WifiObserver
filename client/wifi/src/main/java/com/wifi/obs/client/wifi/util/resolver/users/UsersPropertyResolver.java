package com.wifi.obs.client.wifi.util.resolver.users;

import com.wifi.obs.client.wifi.model.value.BrowseQueryVO;
import java.util.List;

public interface UsersPropertyResolver {

	List<String> resolve(BrowseQueryVO source);
}
