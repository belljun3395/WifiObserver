package com.wifi.obs.client.wifi.dto.response;

import java.util.Optional;

public interface ClientResponse<R> {
	Optional<R> getResponse();

	String getHost();
}
