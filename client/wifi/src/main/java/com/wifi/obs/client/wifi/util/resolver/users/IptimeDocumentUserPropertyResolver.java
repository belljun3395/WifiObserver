package com.wifi.obs.client.wifi.util.resolver.users;

import com.wifi.obs.client.wifi.http.HTMLResponse;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class IptimeDocumentUserPropertyResolver implements UsersPropertyResolver {

	private static final String TBODY = "tbody";
	private static final String TR = "tr";
	private static final String TD = "td";
	private static final String STYLE = "#a9a9a9";

	@Override
	public List<String> resolve(HTMLResponse source) {
		Element body = source.getHttpResponse().getCrawlResponse().getBody().body();

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
