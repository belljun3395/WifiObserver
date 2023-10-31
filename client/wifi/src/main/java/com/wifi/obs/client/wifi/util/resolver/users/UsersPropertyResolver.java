package com.wifi.obs.client.wifi.util.resolver.users;

import com.wifi.obs.client.wifi.http.HTMLResponse;
import java.util.List;

public interface UsersPropertyResolver {

	List<String> resolve(HTMLResponse source);
}
