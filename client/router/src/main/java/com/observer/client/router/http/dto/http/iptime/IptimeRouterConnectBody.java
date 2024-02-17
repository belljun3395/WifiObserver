package com.observer.client.router.http.dto.http.iptime;

import com.observer.client.router.http.dto.http.RouterConnectBody;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.jsoup.nodes.Document;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class IptimeRouterConnectBody extends RouterConnectBody<Document> {}
