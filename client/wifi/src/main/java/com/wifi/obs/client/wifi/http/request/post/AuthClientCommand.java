package com.wifi.obs.client.wifi.http.request.post;

import com.wifi.obs.client.wifi.dto.http.WifiAuthRequestElement;
import com.wifi.obs.client.wifi.http.HTMLResponse;

@FunctionalInterface
public interface AuthClientCommand {

	HTMLResponse command(WifiAuthRequestElement source);
}
