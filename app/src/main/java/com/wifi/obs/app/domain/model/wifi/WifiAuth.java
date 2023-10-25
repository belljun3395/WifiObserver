package com.wifi.obs.app.domain.model.wifi;

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
public class WifiAuth {

	private ModelId id;
	private String host;
	private String certification;
	private String password;

	public Long getId() {
		return id.getId();
	}
}
