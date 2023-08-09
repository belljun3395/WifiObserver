package com.wifi.observer.client.wifi.dto.request;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class WifiBulkBrowseRequest<R extends WifiBrowseRequest> {

	private final List<R> source;
}
