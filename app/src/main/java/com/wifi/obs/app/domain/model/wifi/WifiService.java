package com.wifi.obs.app.domain.model.wifi;

import java.time.LocalDateTime;
import java.util.Objects;
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
public class WifiService {

	private Long id;
	private WifiAuth auth;
	private Long memberId;
	private WifiServiceType type;
	private Long cycle;
	private Long standardTime;
	private WifiStatus status;
	private LocalDateTime createdAt;

	public boolean isOn() {
		return status.isOn();
	}

	public boolean isSameWifiService(Long id) {
		return Objects.equals(this.id, id);
	}

	public Long getAuthId() {
		return auth.getId();
	}

	public String getHost() {
		return auth.getHost();
	}
}
