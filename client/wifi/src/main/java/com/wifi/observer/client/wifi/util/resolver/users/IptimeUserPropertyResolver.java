package com.wifi.observer.client.wifi.util.resolver.users;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class IptimeUserPropertyResolver implements UsersPropertyResolver {

	private static final String TBODY = "tbody";
	private static final String TR = "tr";
	private static final String TD = "td";
	private static final String STYLE = "#a9a9a9";

	@Override
	public List<String> resolve(Optional<Document> source) {
		if (source.isEmpty()) {
			return Collections.emptyList();
		}
		Element body = source.get().body();

		Elements tbody = body.select(TBODY);

		return getAllUsersProperty(tbody);
	}

	private List<String> getAllUsersProperty(Elements source) {
		return source.stream()
				.map(e -> e.select(TR).select(TD))
				.flatMap(Collection::stream)
				.filter(e -> !e.toString().contains(STYLE))
				.map(e -> e.text().replace("-", ":").toLowerCase())
				.collect(Collectors.toList());
	}
}
