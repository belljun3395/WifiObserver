package com.wifi.obs.app.domain.model.member;

import com.wifi.obs.app.domain.model.ModelId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Member {

	private ModelId id;
	private String certification;
	private String password;
	private MemberStatus status;
	private Long deviceMax;
	private Long serviceMax;

	public Long getId() {
		return id.getId();
	}

	public boolean isOverDeviceMax(Long target) {
		return target >= deviceMax;
	}

	public boolean isOverServiceMax(Long target) {
		return target >= serviceMax;
	}

	public boolean isPassword(String target) {
		return password.equals(target);
	}

	public boolean isOwner(Long target) {
		return id.isSame(target);
	}
}
