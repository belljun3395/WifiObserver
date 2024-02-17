package com.observer.client.router.service.iptime;

import com.observer.client.router.exception.ClientException;
import com.observer.client.router.http.client.iptime.IptimeUsersClient;
import com.observer.client.router.http.dto.generator.iptime.IptimeBrowseClientHeaderGenerator;
import com.observer.client.router.http.dto.http.iptime.IptimeRouterConnectBody;
import com.observer.client.router.http.dto.http.iptime.IptimeWifiBrowseClientDto;
import com.observer.client.router.support.dto.request.iptime.IptimeUsersServiceRequest;
import com.observer.client.router.support.dto.response.RouterUser;
import com.observer.client.router.support.dto.response.RouterUsers;
import com.observer.client.router.support.dto.response.RouterUsersResponse;
import com.observer.client.router.util.resolver.users.IptimeRouterUsersOnConnectFilterDecorator;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IptimeUsersService {

	private static final String HTTP = "http://";

	@Value("${iptime.http.login.timepro.query}")
	private String loginTimeproQuery;

	private final IptimeBrowseClientHeaderGenerator iptimeBrowseClientHeaderGenerator;
	private final IptimeRouterUsersOnConnectFilterDecorator iptimeRouterUsersOnConnectFilterDecorator;
	private final IptimeUsersClient iptimeUsersClient;

	public RouterUsersResponse execute(IptimeUsersServiceRequest request) {
		final String host = request.getHost();
		IptimeWifiBrowseClientDto data = getClientDto(request);

		List<RouterUser> users = getUsers(data);

		return RouterUsersResponse.builder().host(host).response(new RouterUsers(users)).build();
	}

	private IptimeWifiBrowseClientDto getClientDto(IptimeUsersServiceRequest request) {
		Map<String, String> headers = iptimeBrowseClientHeaderGenerator.execute(request.getHost());
		return IptimeWifiBrowseClientDto.builder()
				.url(HTTP + request.getHost() + loginTimeproQuery)
				.headers(headers)
				.cookie(request.getAuthInfo())
				.build();
	}

	private List<RouterUser> getUsers(IptimeWifiBrowseClientDto data) {
		IptimeRouterConnectBody clientResponse = null;
		try {
			clientResponse = iptimeUsersClient.execute(data);
		} catch (IOException e) {
			throw new ClientException(e);
		}
		clientResponse = Objects.requireNonNull(clientResponse);
		return iptimeRouterUsersOnConnectFilterDecorator.resolve(clientResponse).stream()
				.map(source -> RouterUser.builder().user(source).build())
				.collect(Collectors.toList());
	}
}
