package com.wifi.obs.client.wifi.model.value;

import com.wifi.obs.client.wifi.http.jsoup.ClientJsoup;
import com.wifi.obs.client.wifi.http.jsoup.HTMLDocumentResponse;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class AuthCommandVO implements ClientHttpVO<HTMLDocumentResponse> {

	private static final HTMLDocumentResponse failDocument = ClientJsoup.getFailDocument();
	private final HTMLDocumentResponse info;

	public static AuthCommandVO fail() {
		return AuthCommandVO.builder().info(failDocument).build();
	}

	public boolean isFail() {
		return info.equals(failDocument);
	}
}
