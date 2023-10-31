package com.wifi.obs.client.wifi.support.generator.iptime;

import static com.wifi.obs.client.wifi.support.log.LogFormat.RESPONSE_ERROR;
import static java.lang.String.format;

import com.wifi.obs.client.wifi.dto.http.WifiBrowseRequestElement;
import com.wifi.obs.client.wifi.dto.response.ClientResponse;
import com.wifi.obs.client.wifi.dto.response.OnConnectUserInfos;
import com.wifi.obs.client.wifi.dto.response.iptime.IptimeOnConnectUserInfosResponse;
import com.wifi.obs.client.wifi.http.HTMLResponse;
import com.wifi.obs.client.wifi.http.request.get.BrowseClientQuery;
import com.wifi.obs.client.wifi.model.Users;
import com.wifi.obs.client.wifi.support.converter.iptime.IptimeBrowseConverter;
import com.wifi.obs.client.wifi.util.resolver.users.IptimeDocumentUsersOnConnectFilterDecorator;
import com.wifi.obs.infra.slack.service.ErrorNotificationService;
import java.util.ArrayList;
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
	private final IptimeDocumentUsersOnConnectFilterDecorator onConnectUsersResolver;
	private final IptimeBrowseConverter iptimeBrowseClientConverter;
	private final Executor requestAsyncExecutor;
	private final ErrorNotificationService errorNotificationService;

	public IptimeBrowseClientFutureGenerator(
			BrowseClientQuery browseClientQuery,
			IptimeDocumentUsersOnConnectFilterDecorator onConnectUsersResolver,
			IptimeBrowseConverter iptimeBrowseClientConverter,
			@Qualifier("wifiobswifirequestAsyncExecutor") Executor requestAsyncExecutor,
			ErrorNotificationService errorNotificationService) {
		this.browseClientQuery = browseClientQuery;
		this.onConnectUsersResolver = onConnectUsersResolver;
		this.iptimeBrowseClientConverter = iptimeBrowseClientConverter;
		this.requestAsyncExecutor = requestAsyncExecutor;
		this.errorNotificationService = errorNotificationService;
	}

	public CompletableFuture<ClientResponse<OnConnectUserInfos>> execute(
			WifiBrowseRequestElement queryDto) {
		final String host = queryDto.getHost();
		return CompletableFuture.supplyAsync(getQueryResponseSupplier(queryDto), requestAsyncExecutor)
				.thenApply(getResponseFunction(host));
	}

	private Supplier<Users> getQueryResponseSupplier(WifiBrowseRequestElement element) {
		return () -> {
			HTMLResponse response = browseClientQuery.query(element);
			if (response.isFail()) {
				writeFailLog(element.getHost());
				return Users.builder().users(new ArrayList<>()).host(element.getHost()).build();
			}
			List<String> resolvedUsers = onConnectUsersResolver.resolve(response);
			return Users.builder().users(resolvedUsers).host(element.getHost()).build();
		};
	}

	private Function<Users, IptimeOnConnectUserInfosResponse> getResponseFunction(String host) {
		return source -> iptimeBrowseClientConverter.from(source.getUsers(), host);
	}

	private void writeFailLog(String host) {
		String className = this.getClass().getName();
		log.warn(RESPONSE_ERROR.getFormat(), className, host);
		errorNotificationService.sendNotification(
				format(RESPONSE_ERROR.getSlackFormat(), className, host));
	}
}
