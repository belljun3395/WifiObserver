package com.wifi.obs.data.mysql.entity.member;

import lombok.Getter;

@Getter
public enum MemberStatus {
	NORMAL(1L, 30L),
	WITHDRAWN(0L, 0L);

	private Long maxServiceCount;
	private Long maxDeviceCount;

	MemberStatus(Long maxServiceCount, Long maxDeviceCount) {
		this.maxServiceCount = maxServiceCount;
		this.maxDeviceCount = maxDeviceCount;
	}
}
