package com.wifi.obs.client.wifi.dto.http;

import com.wifi.obs.client.wifi.dto.function.ExistBody;
import com.wifi.obs.client.wifi.dto.function.ExistHeaders;
import com.wifi.obs.client.wifi.dto.function.ExistHost;

public interface WifiAuthRequestElement
		extends WifiRequestBaseElement, ExistHeaders, ExistBody, ExistHost {}
