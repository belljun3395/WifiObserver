package com.wifi.obs.app.domain.model;

import com.wifi.obs.data.mysql.entity.wifi.service.WifiServiceType;
import com.wifi.obs.data.mysql.entity.wifi.service.WifiStatus;
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
public class WifiServiceModel {

	private Long id;
	private Long memberId;
	private Long authId;
	private WifiServiceType serviceType;
	private Long cycle;
	private Long standardTime;
	private WifiStatus status;
	private LocalDateTime createdAt;

	public boolean isOn() {
		return status.equals(WifiStatus.ON);
	}

	public String getCreateAtAsString() {
		return createdAt.toString();
	}
}
