package com.wifi.obs.client.wifi.http.jsoup;

import com.wifi.obs.client.wifi.http.CrawlAble;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpEntity;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class ClientDocument extends Document implements CrawlAble<HttpEntity<Document>> {

	private final HttpEntity<Document> body;

	private ClientDocument(Document document) {
		super(document.baseUri());
		this.body = new HttpEntity<>(document);
	}

	public static ClientDocument of(Document document) {
		return new ClientDocument(document);
	}

	@Override
	public HttpEntity<Document> getCrawlResponse() {
		return body;
	}
}
