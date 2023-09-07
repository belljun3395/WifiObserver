package com.wifi.obs.app.domain.service.wifi;

import com.wifi.obs.app.domain.dto.response.service.OnConnectUserInfos;
import com.wifi.obs.data.mysql.entity.wifi.auth.WifiAuthEntity;

public interface GetUserService {

	OnConnectUserInfos execute(WifiAuthEntity authInfo);
}
