package com.wifi.obs.app.domain.dto.response.service;

import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

// beta 때문에 추가
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OnConnectUserInfos {

	private final List<UserInfo> users;

	public static OnConnectUserInfos of(List<UserInfo> infos) {
		return new OnConnectUserInfos(infos);
	}
}
