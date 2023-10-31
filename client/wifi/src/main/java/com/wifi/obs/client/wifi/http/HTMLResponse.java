package com.wifi.obs.client.wifi.http;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.springframework.http.HttpEntity;

@Getter
@ToString
@EqualsAndHashCode
public class HTMLResponse implements DocumentResponse {

	private static final Document FAIL_DOCUMENT = Parser.parse(Strings.EMPTY, Strings.EMPTY);
	private static final String FAIL_BODY = FAIL_DOCUMENT.toString();

	private final CrawlAble<HttpEntity<Document>> httpResponse;

	private HTMLResponse(CrawlAble<HttpEntity<Document>> httpResponse) {
		this.httpResponse = httpResponse;
	}

	public static HTMLResponse of(CrawlAble<HttpEntity<Document>> document) {
		return new HTMLResponse(document);
	}

	public static HTMLResponse fail() {
		return HTMLResponse.of(() -> new HttpEntity<>(FAIL_DOCUMENT));
	}

	public boolean isFail() {
		return FAIL_BODY.equals(httpResponse.getCrawlResponse().getBody().toString());
	}
}
