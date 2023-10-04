package com.wifi.obs.app.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.wifi.obs.app.domain.model.member.Member;
import com.wifi.obs.app.domain.model.member.MemberStatus;
import org.junit.jupiter.api.Test;

class MemberTest {

	static Long memberId = 1L;
	static String certification = "certification";
	static String password = "password";
	static MemberStatus status = MemberStatus.NORMAL;
	static Long deviceMaxCount = MemberStatus.NORMAL.getMaxDeviceCount();
	static Long serviceMaxCount = MemberStatus.NORMAL.getMaxServiceCount();

	@Test
	void isOverDeviceMaxCount() {
		// given
		Member member =
				Member.builder()
						.id(memberId)
						.password(password)
						.status(status)
						.deviceMax(deviceMaxCount)
						.serviceMax(serviceMaxCount)
						.build();

		// when
		boolean falseResult = member.isOverDeviceMax(deviceMaxCount - 1);
		boolean trueResult = member.isOverDeviceMax(deviceMaxCount);

		// then
		assertFalse(falseResult);
		assertTrue(trueResult);
	}

	@Test
	void isOverServiceMaxCount() {
		// given
		Member member =
				Member.builder()
						.id(memberId)
						.password(password)
						.status(status)
						.deviceMax(deviceMaxCount)
						.serviceMax(serviceMaxCount)
						.build();

		// when
		boolean falseResult = member.isOverServiceMax(serviceMaxCount - 1);
		boolean trueResult = member.isOverServiceMax(serviceMaxCount);

		// then
		assertFalse(falseResult);
		assertTrue(trueResult);
	}

	@Test
	void isSamePassword() {
		// given
		Member member =
				Member.builder()
						.id(memberId)
						.password(password)
						.status(status)
						.deviceMax(deviceMaxCount)
						.serviceMax(serviceMaxCount)
						.build();

		// when
		boolean falseResult = member.isPassword(password + "false");
		boolean trueResult = member.isPassword(password);

		// then
		assertFalse(falseResult);
		assertTrue(trueResult);
	}
}
