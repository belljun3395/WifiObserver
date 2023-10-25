package com.wifi.obs.client.wifi.http.request.get;

import com.wifi.obs.client.wifi.dto.http.WifiBrowseRequestElement;
import com.wifi.obs.client.wifi.model.Users;

@FunctionalInterface
public interface BrowseClientQuery {

	Users query(WifiBrowseRequestElement source);
}
