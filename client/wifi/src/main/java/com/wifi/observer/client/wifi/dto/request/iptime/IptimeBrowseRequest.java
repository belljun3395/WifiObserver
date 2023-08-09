package com.wifi.observer.client.wifi.dto.request.iptime;

import com.wifi.observer.client.wifi.dto.request.WifiBrowseRequest;
import com.wifi.observer.client.wifi.support.log.HostLogAble;
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
public class IptimeBrowseRequest implements WifiBrowseRequest, HostLogAble {
	private final String authInfo;
	private final String host;
}
