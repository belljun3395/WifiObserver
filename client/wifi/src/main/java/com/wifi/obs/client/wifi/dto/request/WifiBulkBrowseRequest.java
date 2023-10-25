package com.wifi.obs.client.wifi.dto.request;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class WifiBulkBrowseRequest<R extends WifiBrowseRequest>
		implements WifiBulkRequest<R> {

	private final List<R> source;
}
