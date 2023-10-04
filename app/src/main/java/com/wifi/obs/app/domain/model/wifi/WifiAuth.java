package com.wifi.obs.app.domain.model.wifi;

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
public class WifiAuth {
	private Long id;
	private String host;
	private String certification;
	private String password;
}
