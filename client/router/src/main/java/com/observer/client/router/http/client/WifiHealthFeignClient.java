package com.observer.client.router.http.client;

import java.net.URI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "health-client", url = "send to parameter URI")
public interface WifiHealthFeignClient {

	@GetMapping
	ResponseEntity<String> query(URI uri);
}
