package com.wifi.obs.client.wifi.support.generator.iptime;

import com.wifi.obs.client.wifi.dto.http.WifiBrowseRequestElement;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.dto.response.OnConnectUserInfos;
import com.wifi.obs.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
import com.wifi.obs.client.wifi.http.request.get.BrowseClientQuery;
import com.wifi.obs.client.wifi.model.Users;
import com.wifi.obs.client.wifi.support.converter.iptime.IptimeBrowseConverter;
import com.wifi.obs.client.wifi.util.resolver.users.IptimeUsersOnConnectFilterDecorator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IptimeBrowseClientFutureGenerator {

	private final BrowseClientQuery browseClientQuery;
	private final IptimeUsersOnConnectFilterDecorator onConnectUsersResolver;
	private final IptimeBrowseConverter iptimeBrowseClientConverter;
	private final Executor requestAsyncExecutor;

	public IptimeBrowseClientFutureGenerator(
			BrowseClientQuery browseClientQuery,
			IptimeUsersOnConnectFilterDecorator onConnectUsersResolver,
			IptimeBrowseConverter iptimeBrowseClientConverter,
			@Qualifier("wifiobswifirequestAsyncExecutor") Executor requestAsyncExecutor) {
		this.browseClientQuery = browseClientQuery;
		this.onConnectUsersResolver = onConnectUsersResolver;
		this.iptimeBrowseClientConverter = iptimeBrowseClientConverter;
		this.requestAsyncExecutor = requestAsyncExecutor;
	}

	public CompletableFuture<ClientResponse<OnConnectUserInfos>> execute(
			WifiBrowseRequestElement queryDto) {
		final String host = queryDto.getHost();
		return CompletableFuture.supplyAsync(getQueryResponseSupplier(queryDto), requestAsyncExecutor)
				.thenApply(getResolveResponseFunction())
				.thenApply(getResponseFunction(host));
	}

	private Supplier<Users> getQueryResponseSupplier(WifiBrowseRequestElement queryDto) {
		return () -> browseClientQuery.query(queryDto);
	}

	private Function<Users, List<String>> getResolveResponseFunction() {
		return response -> onConnectUsersResolver.resolve(response.getUsersInfo());
	}

	private Function<List<String>, IptimeOnConnectUserInfosResponse> getResponseFunction(
			String host) {
		return users -> iptimeBrowseClientConverter.from(users, host);
	}
}
