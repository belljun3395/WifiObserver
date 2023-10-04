package com.wifi.obs.app.domain.dto.response.service;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class WifiServiceInfos {

	private final List<WifiServiceInfo> services;

	public static WifiServiceInfos of(List<WifiServiceInfo> infos) {
		return new WifiServiceInfos(infos);
	}
}
