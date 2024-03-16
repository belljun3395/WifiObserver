package com.observer.domain.external.client.connection;

import com.observer.client.router.exception.ClientAuthException;
import com.observer.client.router.exception.ClientException;
import com.observer.client.router.service.iptime.IptimeAuthService;
import com.observer.client.router.service.iptime.IptimeHealthService;
import com.observer.client.router.service.iptime.IptimeUsersService;
import com.observer.client.router.support.dto.request.WifiHealthServiceRequest;
import com.observer.client.router.support.dto.request.iptime.IptimeAuthServiceRequest;
import com.observer.client.router.support.dto.request.iptime.IptimeUsersServiceRequest;
import com.observer.client.router.support.dto.response.RouterAuthResponse;
import com.observer.client.router.support.dto.response.RouterHealthResponse;
import com.observer.client.router.support.dto.response.RouterUsersResponse;
import com.observer.domain.external.client.dto.connection.ConnectedUser;
import com.observer.domain.external.client.dto.connection.ConnectedUsers;
import com.observer.domain.external.client.dto.connection.ConnectedUsersRequest;
import com.observer.domain.external.client.dto.connection.ConnectedUsersResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetIptimeConnectionService implements GetConnectionService {

	private final IptimeHealthService iptimeHealthService;
	private final IptimeAuthService iptimeAuthService;
	private final IptimeUsersService iptimeUsersService;

	@Override
	public ConnectedUsersResponse execute(ConnectedUsersRequest request) {
		final String ip = request.getIp();
		final Long port = request.getPort();
		final String userName = request.getCertification();
		final String password = request.getPassword();

		WifiHealthServiceRequest hostRequest =
				WifiHealthServiceRequest.builder().host(getHost(ip, port)).build();
		RouterHealthResponse healthResponse = null;
		try {
			healthResponse = iptimeHealthService.execute(hostRequest);
		} catch (ClientException e) {
			throw new IllegalStateException("router is not connected");
		}
		if (healthResponse.getResponse().is4xxClientError()) {
			throw new IllegalStateException("router is not connected");
		}

		IptimeAuthServiceRequest authRequest =
				IptimeAuthServiceRequest.builder()
						.host(getHost(ip, port))
						.userName(userName)
						.password(password)
						.build();
		RouterAuthResponse authResponse = null;
		try {
			authResponse = iptimeAuthService.execute(authRequest);
		} catch (ClientAuthException | ClientException e) {
			throw new RuntimeException(e);
		}

		IptimeUsersServiceRequest browseRequest =
				IptimeUsersServiceRequest.builder()
						.authInfo(authResponse.getResponse().getAuth())
						.host(getHost(ip, port))
						.build();
		RouterUsersResponse usersResponse = null;
		try {
			usersResponse = iptimeUsersService.execute(browseRequest);
		} catch (ClientException e) {
			throw new RuntimeException(e);
		}
		ConnectedUsers connectedUsers = toConnectedUsers(usersResponse);
		return ConnectedUsersResponse.builder().host(ip).response(connectedUsers).build();
	}

	private String getHost(String ip, Long port) {
		return ip + ":" + port;
	}

	private ConnectedUsers toConnectedUsers(RouterUsersResponse usersResponse) {
		List<ConnectedUser> users =
				usersResponse.getResponse().getUsers().stream()
						.map(routerUser -> ConnectedUser.builder().user(routerUser.getUser()).build())
						.collect(Collectors.toList());
		return ConnectedUsers.builder().users(users).build();
	}
}
