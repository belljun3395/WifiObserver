package com.observer.domain.usecase.connection;

import com.observer.domain.dto.connection.IptimeCurrentConnectionUseCaseRequest;
import com.observer.domain.dto.connection.IptimeCurrentConnectionUseCaseResponse;
import com.observer.domain.external.client.connection.GetConnectionService;
import com.observer.domain.external.client.dto.connection.ConnectedUsersRequest;
import com.observer.domain.external.client.dto.connection.ConnectedUsersResponse;
import com.observer.domain.service.connection.GetConnectionMemberIdQuery;
import com.observer.domain.service.connection.GetConnectionMemberRouterHostInfoQuery;
import com.observer.domain.service.connection.dto.RouterHostInfo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetIptimeConnectionUsersUseCase {

	private final GetConnectionMemberIdQuery getConnectionMemberIdQuery;
	private final GetConnectionMemberRouterHostInfoQuery getConnectionMemberRouterHostInfoQuery;

	private final GetConnectionService getIptimeConnectionService;

	@Transactional(readOnly = true)
	public IptimeCurrentConnectionUseCaseResponse execute(
			IptimeCurrentConnectionUseCaseRequest request) {
		final String apiKey = request.getApiKey();
		final Long routerId = request.getRouterId();

		Long memberId = getConnectionMemberIdQuery.execute(apiKey);
		Optional<RouterHostInfo> routerHostInfoSource =
				getConnectionMemberRouterHostInfoQuery.execute(routerId, memberId);
		if (routerHostInfoSource.isEmpty()) {
			throw new IllegalArgumentException("not found router info");
		}
		RouterHostInfo routerHostInfo = routerHostInfoSource.get();
		if (!routerHostInfo.getServiceType().equals("IPTIME")) {
			throw new IllegalArgumentException("not supported router type");
		}

		ConnectedUsersRequest connectedUsersRequest =
				ConnectedUsersRequest.builder()
						.ip(routerHostInfo.getIp())
						.port(routerHostInfo.getPort())
						.certification(routerHostInfo.getCertification())
						.password(routerHostInfo.getPassword())
						.build();
		ConnectedUsersResponse connectedUsersResponse =
				getIptimeConnectionService.execute(connectedUsersRequest);

		return IptimeCurrentConnectionUseCaseResponse.builder()
				.host(connectedUsersResponse.getHost())
				.infos(connectedUsersResponse.getResponse())
				.build();
	}
}
