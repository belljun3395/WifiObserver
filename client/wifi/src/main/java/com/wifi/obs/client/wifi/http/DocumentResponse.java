package com.wifi.obs.client.wifi.http;

import org.jsoup.nodes.Document;
import org.springframework.http.HttpEntity;

public interface DocumentResponse extends HttpResponse<CrawlAble<HttpEntity<Document>>> {}
