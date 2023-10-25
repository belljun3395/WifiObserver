package com.wifi.obs.client.wifi.dto.request.iptime;

import com.wifi.obs.client.wifi.dto.request.WifiBrowseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
public class IptimeBrowseRequest implements WifiBrowseRequest {

	private final String authInfo;
	private final String host;
}
