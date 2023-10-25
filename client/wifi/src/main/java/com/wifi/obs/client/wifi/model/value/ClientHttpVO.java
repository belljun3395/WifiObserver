package com.wifi.obs.client.wifi.model.value;

import com.wifi.obs.client.wifi.http.HttpResponse;

public interface ClientHttpVO<T extends HttpResponse> {

	T getInfo();
}
