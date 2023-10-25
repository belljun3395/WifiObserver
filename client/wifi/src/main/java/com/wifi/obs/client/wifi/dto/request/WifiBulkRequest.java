package com.wifi.obs.client.wifi.dto.request;

import com.wifi.obs.client.wifi.support.log.LogAbleBulkRequest;
import java.util.List;

public interface WifiBulkRequest<R extends WifiClientRequest> extends LogAbleBulkRequest {

	@Override
	List<R> getSource();
}
