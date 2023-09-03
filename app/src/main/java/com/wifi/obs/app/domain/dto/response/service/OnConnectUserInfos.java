package com.wifi.obs.app.domain.dto.response.service;

import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data // beta 때문에 추가
@RequiredArgsConstructor
public class OnConnectUserInfos {

	private final List<UserInfo> userInfos;
}
