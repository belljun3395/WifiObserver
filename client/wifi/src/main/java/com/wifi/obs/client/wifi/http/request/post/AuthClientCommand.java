package com.wifi.obs.client.wifi.http.request.post;

import com.wifi.obs.client.wifi.dto.http.WifiAuthRequestElement;
import com.wifi.obs.client.wifi.model.Auth;

@FunctionalInterface
public interface AuthClientCommand {

	Auth command(WifiAuthRequestElement source);
}
