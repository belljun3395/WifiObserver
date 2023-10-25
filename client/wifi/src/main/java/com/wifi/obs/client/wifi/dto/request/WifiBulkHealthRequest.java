package com.wifi.obs.client.wifi.dto.request;

import com.wifi.obs.client.wifi.support.log.LogAbleBulkRequest;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class WifiBulkHealthRequest<T extends WifiHealthRequest>
		implements LogAbleBulkRequest {

	private final List<T> source;
}
