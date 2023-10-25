package com.wifi.obs.client.wifi.support.log;

import com.wifi.obs.client.wifi.dto.function.ExistHost;
import java.util.List;

public interface LogAbleBulkRequest {

	List<? extends ExistHost> getSource();
}
