package com.wifi.observer.client.wifi.util.resolver.users;

import java.util.List;
import java.util.Optional;
import org.jsoup.nodes.Document;

public interface UsersPropertyResolver {

	/**
	 * 사용자 정보를 추출한다.
	 *
	 * @param source 사용자 정보가 포함된 HTML
	 * @return 사용자 정보
	 */
	List<String> resolve(Optional<Document> source);
}
