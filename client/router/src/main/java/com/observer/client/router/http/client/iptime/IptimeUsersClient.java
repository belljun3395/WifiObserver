package com.observer.client.router.http.client.iptime;

import com.observer.client.router.http.client.UsersClient;
import com.observer.client.router.http.dto.http.iptime.IptimeRouterConnectBody;
import com.observer.client.router.http.dto.http.iptime.IptimeWifiBrowseClientDto;
import java.io.IOException;
import org.jsoup.nodes.Document;

public interface IptimeUsersClient extends UsersClient<IptimeWifiBrowseClientDto, Document> {

	@Override
	IptimeRouterConnectBody execute(IptimeWifiBrowseClientDto source) throws IOException;
}
