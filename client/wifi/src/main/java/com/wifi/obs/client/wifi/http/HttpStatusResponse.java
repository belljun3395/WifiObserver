package com.wifi.obs.client.wifi.http;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class HttpStatusResponse implements HttpResponse<HttpStatus> {

	private final HttpStatus response;

	public static HttpStatusResponse of(HttpStatus status) {
		return new HttpStatusResponse(status);
	}
}
