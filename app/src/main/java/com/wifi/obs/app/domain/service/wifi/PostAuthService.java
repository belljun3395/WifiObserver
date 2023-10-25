package com.wifi.obs.app.domain.service.wifi;

import com.wifi.obs.client.wifi.dto.response.AuthInfo;

public interface PostAuthService {

	AuthInfo execute(String host, String userName, String password);
}
