package com.wifi.observer.client.wifi.support.jsoup;

import java.util.Objects;
import lombok.Getter;
import lombok.ToString;
import org.jsoup.nodes.Document;

@Getter
@ToString
public class ClientDocument extends Document {

	private final Document document;

	private ClientDocument(Document document) {
		super(document.baseUri());
		this.document = document;
	}

	public static ClientDocument of(Document document) {
		return new ClientDocument(document);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		ClientDocument that = (ClientDocument) o;
		return Objects.equals(document, that.document);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), document);
	}
}
