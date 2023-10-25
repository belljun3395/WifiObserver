package com.wifi.obs.client.wifi.dto.http;

import com.wifi.obs.client.wifi.dto.function.ExistCookie;
import com.wifi.obs.client.wifi.dto.function.ExistHeaders;
import com.wifi.obs.client.wifi.dto.function.ExistHost;

public interface WifiBrowseRequestElement
		extends WifiRequestBaseElement, ExistHeaders, ExistCookie, ExistHost {}
