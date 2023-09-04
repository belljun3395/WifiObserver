package com.wifi.obs.app.domain.service.wifi;

import org.springframework.http.HttpStatus;

public interface GetHealthService {

	HttpStatus execute(String host);
}
