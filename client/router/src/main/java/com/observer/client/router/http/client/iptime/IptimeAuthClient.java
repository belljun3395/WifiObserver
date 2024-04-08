package com.observer.client.router.http.client.iptime;

import com.observer.client.router.http.client.AuthClient;
import com.observer.client.router.http.dto.http.iptime.IptimeRouterConnectBody;
import com.observer.client.router.http.dto.http.iptime.IptimeWifiAuthClientDto;
import java.io.IOException;
import org.jsoup.nodes.Document;

public interface IptimeAuthClient extends AuthClient<IptimeWifiAuthClientDto, Document> {

	@Override
	IptimeRouterConnectBody execute(IptimeWifiAuthClientDto source) throws IOException;
}
