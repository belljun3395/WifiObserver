package com.wifi.obs.app.domain.service.wifi;

import com.wifi.observer.client.wifi.dto.response.AuthInfo;

public interface PostAuthService {

	AuthInfo execute(String host, String userName, String password);
}
