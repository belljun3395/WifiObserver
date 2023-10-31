package com.wifi.obs.client.wifi.http.jsoup;

import org.apache.logging.log4j.util.Strings;
import org.jsoup.parser.Parser;

public class ClientJsoup {

	public static ClientDocument getFailDocument() {
		return ClientJsoup.parse(Strings.EMPTY);
	}

	private static ClientDocument parse(String html) {
		return ClientDocument.of(Parser.parse(html, Strings.EMPTY));
	}

	public static ClientHttpConnection connect(String url) {
		return ClientHttpConnection.connect(url);
	}
}
