package com.wifi.obs.client.wifi.http.jsoup;

import com.wifi.obs.client.wifi.http.HttpResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jsoup.nodes.Document;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class HTMLDocumentResponse extends Document implements HttpResponse<Document> {

	private final Document response;

	private HTMLDocumentResponse(Document response) {
		super(response.baseUri());
		this.response = response;
	}

	public static HTMLDocumentResponse of(Document document) {
		return new HTMLDocumentResponse(document);
	}
}
