package com.wifi.obs.app.domain.dto.response.service;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class OnConnectUserInfos {

	private final List<UserInfo> userInfos;
}
