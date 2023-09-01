package com.wifi.obs.app.security.authentication.authority;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@Getter
@ToString
public enum Roles {
	USER("ROLE_USER") {
		@Override
		public GrantedAuthority getAuthority() {
			return UserAuthority.builder().build();
		}
	};

	Roles(String role) {
		this.role = role;
	}

	private final String role;

	public abstract GrantedAuthority getAuthority();

	public static Roles roleOf(String role) {
		for (Roles roles : Roles.values()) {
			if (roles.getRole().equals(role)) {
				return roles;
			}
		}
		throw new IllegalArgumentException("Invalid role. role: " + role);
	}
}
