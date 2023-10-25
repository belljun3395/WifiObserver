package com.wifi.obs.client.wifi.http.jsoup;

import org.apache.logging.log4j.util.Strings;
import org.jsoup.parser.Parser;

public class ClientJsoup {

	public static HTMLDocumentResponse getFailDocument() {
		return ClientJsoup.parse(Strings.EMPTY);
	}

	private static HTMLDocumentResponse parse(String html) {
		return HTMLDocumentResponse.of(Parser.parse(html, ""));
	}

	public static ClientHttpConnection connect(String url) {
		return ClientHttpConnection.connect(url);
	}
}
