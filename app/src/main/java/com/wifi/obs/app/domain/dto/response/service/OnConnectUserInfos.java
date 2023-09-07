package com.wifi.obs.app.domain.dto.response.service;

import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data // beta 때문에 추가
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OnConnectUserInfos {

	private final List<UserInfo> userInfos;

	public static OnConnectUserInfos of(List<UserInfo> userInfos) {
		return new OnConnectUserInfos(userInfos);
	}
}
