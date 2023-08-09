package com.wifi.observer.client.wifi.dto.request.common;

import com.wifi.observer.client.wifi.dto.request.WifiHostRequest;
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
public class CommonWifiHealthRequest implements WifiHostRequest, HostLogAble {

	private final String host;
}
