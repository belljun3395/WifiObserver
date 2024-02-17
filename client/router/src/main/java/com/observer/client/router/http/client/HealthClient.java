package com.observer.client.router.http.client;

import com.observer.client.router.http.dto.http.RouterConnectStatus;
import java.io.IOException;

public interface HealthClient<T> {

	RouterConnectStatus execute(T source) throws IOException;
}
