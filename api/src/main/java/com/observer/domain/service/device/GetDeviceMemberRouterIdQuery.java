package com.observer.domain.service.device;

import com.observer.domain.service.router.support.GetRouterInfoSupportQuery;
import com.observer.domain.service.router.support.RouterInfoSupport;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetDeviceMemberRouterIdQuery {

	private final GetRouterInfoSupportQuery getRouterInfoSupportQuery;

	public Optional<Long> execute(Long routerId, Long memberId) {
		RouterInfoSupport routerInfoSupport =
				getRouterInfoSupportQuery.execute(routerId).orElseThrow(IllegalStateException::new);
		if (!routerInfoSupport.getMemberId().equals(memberId)) {
			return Optional.empty();
		}
		return Optional.of(routerInfoSupport.getRouterId());
	}
}
