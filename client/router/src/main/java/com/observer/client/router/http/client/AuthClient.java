package com.observer.client.router.http.client;

import com.observer.client.router.http.dto.http.RouterConnectBody;
import java.io.IOException;

public interface AuthClient<T, R> {

	RouterConnectBody<R> execute(T source) throws IOException;
}
