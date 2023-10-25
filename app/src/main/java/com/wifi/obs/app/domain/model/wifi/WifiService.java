package com.wifi.obs.app.domain.model.wifi;

import com.wifi.obs.app.domain.model.ModelId;
import java.time.LocalDateTime;
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

	private ModelId id;
	private WifiAuth auth;
	private ModelId memberId;
	private WifiServiceType type;
	private Long cycle;
	private Long standardTime;
	private WifiStatus status;
	private LocalDateTime createdAt;

	public Long getId() {
		return id.getId();
	}

	public Long getMemberId() {
		return memberId.getId();
	}

	public Long getAuthId() {
		return auth.getId();
	}

	public String getHost() {
		return auth.getHost();
	}

	public boolean isOn() {
		return status.isOn();
	}

	public boolean isSameWifiService(Long id) {
		return this.id.isSame(id);
	}

	public boolean isServiceOwner(Long id) {
		return memberId.isSame(id);
	}
}
