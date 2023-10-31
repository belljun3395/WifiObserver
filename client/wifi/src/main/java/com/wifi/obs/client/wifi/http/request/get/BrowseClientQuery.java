package com.wifi.obs.client.wifi.http.request.get;

import com.wifi.obs.client.wifi.dto.http.WifiBrowseRequestElement;
import com.wifi.obs.client.wifi.http.HTMLResponse;

@FunctionalInterface
public interface BrowseClientQuery {

	HTMLResponse query(WifiBrowseRequestElement source);
}
