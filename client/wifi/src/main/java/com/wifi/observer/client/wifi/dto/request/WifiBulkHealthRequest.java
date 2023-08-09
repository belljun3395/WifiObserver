package com.wifi.observer.client.wifi.dto.request;

import com.wifi.observer.client.wifi.dto.request.common.CommonWifiHealthRequest;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class WifiBulkHealthRequest {

	private final List<CommonWifiHealthRequest> source;
}
