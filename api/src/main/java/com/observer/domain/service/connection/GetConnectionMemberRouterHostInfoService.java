package com.observer.domain.service.connection;

import com.observer.domain.service.connection.dto.RouterHostInfo;
import com.observer.domain.service.router.support.GetRouterInfoSupportService;
import com.observer.domain.service.router.support.RouterInfoSupport;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetConnectionMemberRouterHostInfoService {

	private final GetRouterInfoSupportService getRouterInfoSupportService;

	public Optional<RouterHostInfo> execute(Long routerId, Long memberId) {
		RouterInfoSupport routerInfoSupport =
				getRouterInfoSupportService.execute(routerId).orElseThrow(IllegalStateException::new);
		if (!routerInfoSupport.getMemberId().equals(memberId)) {
			return Optional.empty();
		}
		String host = routerInfoSupport.getHost();
		String ip = host.split(":")[0];
		Long port = Long.parseLong(host.split(":")[1]);
		return Optional.of(
				RouterHostInfo.builder()
						.routerId(routerInfoSupport.getRouterId())
						.ip(ip)
						.port(port)
						.certification(routerInfoSupport.getCertification())
						.password(routerInfoSupport.getPassword())
						.build());
	}
}
