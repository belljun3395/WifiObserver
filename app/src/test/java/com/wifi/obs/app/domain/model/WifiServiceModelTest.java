package com.wifi.obs.app.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceType;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiStatus;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class WifiServiceModelTest {

	static Long serviceId = 1L;
	static Long memberId = 1L;
	static Long authId = 1L;
	static WifiServiceType serviceType = WifiServiceType.IPTIME;
	static Long cycle = 10L;
	static Long standardTime = 9L;
	static WifiStatus status = WifiStatus.ON;
	static LocalDateTime createdAt = LocalDateTime.now();

	@Test
	void isOn_O() {
		// given
		WifiServiceModel service =
				WifiServiceModel.builder()
						.id(serviceId)
						.memberId(memberId)
						.authId(authId)
						.serviceType(serviceType)
						.cycle(cycle)
						.standardTime(standardTime)
						.status(status)
						.createdAt(createdAt)
						.build();

		// when
		boolean result = service.isOn();

		// then
		assertTrue(result);
	}

	@Test
	void isOn_X() {
		// given
		WifiServiceModel service =
				WifiServiceModel.builder()
						.id(serviceId)
						.memberId(memberId)
						.authId(authId)
						.serviceType(serviceType)
						.cycle(cycle)
						.standardTime(standardTime)
						.status(WifiStatus.ERROR)
						.createdAt(createdAt)
						.build();

		// when
		boolean result = service.isOn();

		// then
		assertFalse(result);
	}
}
