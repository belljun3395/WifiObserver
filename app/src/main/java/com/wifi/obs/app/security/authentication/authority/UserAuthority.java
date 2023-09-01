package com.wifi.obs.app.security.authentication.authority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@Getter
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
class UserAuthority implements GrantedAuthority {

	@Override
	public String getAuthority() {
		return Roles.USER.getRole();
	}
}
