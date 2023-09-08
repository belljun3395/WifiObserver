package com.wifi.obs.app.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.wifi.obs.data.mysql.entity.member.MemberStatus;
import org.junit.jupiter.api.Test;

class MemberModelTest {

	static Long memberId = 1L;
	static String certification = "certification";
	static String password = "password";
	static MemberStatus status = MemberStatus.NORMAL;
	static Long deviceMaxCount = MemberStatus.NORMAL.getMaxDeviceCount();
	static Long serviceMaxCount = MemberStatus.NORMAL.getMaxServiceCount();

	@Test
	void isOverDeviceMaxCount() {
		// given
		MemberModel member =
				MemberModel.builder()
						.id(memberId)
						.password(password)
						.status(status)
						.deviceMaxCount(deviceMaxCount)
						.serviceMaxCount(serviceMaxCount)
						.build();

		// when
		boolean falseResult = member.isOverDeviceMaxCount(deviceMaxCount - 1);
		boolean trueResult = member.isOverDeviceMaxCount(deviceMaxCount);

		// then
		assertFalse(falseResult);
		assertTrue(trueResult);
	}

	@Test
	void isOverServiceMaxCount() {
		// given
		MemberModel member =
				MemberModel.builder()
						.id(memberId)
						.password(password)
						.status(status)
						.deviceMaxCount(deviceMaxCount)
						.serviceMaxCount(serviceMaxCount)
						.build();

		// when
		boolean falseResult = member.isOverServiceMaxCount(serviceMaxCount - 1);
		boolean trueResult = member.isOverServiceMaxCount(serviceMaxCount);

		// then
		assertFalse(falseResult);
		assertTrue(trueResult);
	}

	@Test
	void isSamePassword() {
		// given
		MemberModel member =
				MemberModel.builder()
						.id(memberId)
						.password(password)
						.status(status)
						.deviceMaxCount(deviceMaxCount)
						.serviceMaxCount(serviceMaxCount)
						.build();

		// when
		boolean falseResult = member.isSamePassword(password + "false");
		boolean trueResult = member.isSamePassword(password);

		// then
		assertFalse(falseResult);
		assertTrue(trueResult);
	}
}
