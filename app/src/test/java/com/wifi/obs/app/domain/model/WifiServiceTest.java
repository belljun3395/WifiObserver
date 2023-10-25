package com.wifi.obs.app.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.wifi.obs.app.domain.model.wifi.WifiAuth;
import com.wifi.obs.app.domain.model.wifi.WifiService;
import com.wifi.obs.app.domain.model.wifi.WifiServiceType;
import com.wifi.obs.app.domain.model.wifi.WifiStatus;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class WifiServiceTest {

	static Long serviceId = 1L;
	static Long memberId = 1L;
	static WifiAuth auth =
			WifiAuth.builder()
					.id(ModelId.of(1L))
					.host("host")
					.certification("certification")
					.password("password")
					.build();
	static WifiServiceType serviceType = WifiServiceType.IPTIME;
	static Long cycle = 10L;
	static Long standardTime = 9L;
	static WifiStatus status = WifiStatus.ON;
	static LocalDateTime createdAt = LocalDateTime.now();

	@Test
	void isOn_O() {
		// given
		WifiService service =
				WifiService.builder()
						.id(ModelId.of(serviceId))
						.memberId(ModelId.of(memberId))
						.auth(auth)
						.type(serviceType)
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
		WifiService service =
				WifiService.builder()
						.id(ModelId.of(serviceId))
						.memberId(ModelId.of(memberId))
						.auth(auth)
						.type(serviceType)
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
