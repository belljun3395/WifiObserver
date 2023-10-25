package com.wifi.obs.client.wifi.dto.request;

import com.wifi.obs.client.wifi.dto.function.ExistPassword;
import com.wifi.obs.client.wifi.dto.function.ExistUserName;

public interface WifiAuthRequest extends WifiClientRequest, ExistUserName, ExistPassword {}
