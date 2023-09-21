package com.wifi.observer.client.wifi.model.info;

import com.wifi.observer.client.wifi.support.jsoup.ClientDocument;
import com.wifi.observer.client.wifi.support.jsoup.ClientJsoup;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class AuthCommandInfo implements DocumentInfo {

	private static final ClientDocument failDocument = ClientJsoup.getFailDocument();
	private final ClientDocument info;

	public static AuthCommandInfo fail() {
		return AuthCommandInfo.builder().info(failDocument).build();
	}

	public boolean isFail() {
		return info.equals(failDocument);
	}
}
