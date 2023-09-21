package com.wifi.observer.client.wifi.support.generator.iptime;

import com.wifi.observer.client.wifi.dto.http.IptimeWifiBrowseClientDto;
import com.wifi.observer.client.wifi.dto.response.ClientResponse;
import com.wifi.observer.client.wifi.dto.response.OnConnectUserInfos;
import com.wifi.observer.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
import com.wifi.observer.client.wifi.http.request.get.BrowseClientQuery;
import com.wifi.observer.client.wifi.model.BrowseQueryClientModel;
import com.wifi.observer.client.wifi.support.converter.iptime.IptimeBrowseConverter;
import com.wifi.observer.client.wifi.util.resolver.users.IptimeUsersOnConnectFilterDecorator;
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
			IptimeWifiBrowseClientDto queryDto) {
		final String host = queryDto.getHost();
		return CompletableFuture.supplyAsync(getQueryResponseSupplier(queryDto), requestAsyncExecutor)
				.thenApply(getResolveResponseFunction())
				.thenApply(getResponseFunction(host));
	}

	private Supplier<BrowseQueryClientModel> getQueryResponseSupplier(
			IptimeWifiBrowseClientDto queryDto) {
		return () -> browseClientQuery.query(queryDto);
	}

	private Function<BrowseQueryClientModel, List<String>> getResolveResponseFunction() {
		return response -> onConnectUsersResolver.resolve(response.getUsersInfo());
	}

	private Function<List<String>, IptimeOnConnectUserInfosResponse> getResponseFunction(
			String host) {
		return users -> iptimeBrowseClientConverter.from(users, host);
	}
}
