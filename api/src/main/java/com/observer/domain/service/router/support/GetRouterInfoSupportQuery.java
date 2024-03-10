package com.observer.domain.service.router.support;

import com.observer.data.entity.router.RouterEntity;
import com.observer.data.entity.router.RouterStatus;
import com.observer.domain.external.dao.router.RouterDao;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetRouterInfoSupportQuery {

	private final RouterDao routerDao;

	public Optional<RouterInfoSupport> execute(Long routerId) {
		Optional<RouterEntity> routerEntitySource =
				routerDao.findByIdAndStatusAndDeletedFalse(routerId, RouterStatus.ON);
		if (routerEntitySource.isEmpty()) {
			return Optional.empty();
		}
		RouterEntity routerEntity = routerEntitySource.get();
		return Optional.of(
				RouterInfoSupport.builder()
						.memberId(routerEntity.getMemberId())
						.routerId(routerEntity.getId())
						.serviceType(routerEntity.getServiceType().getType())
						.cycle(routerEntity.getCycle())
						.standardTime(routerEntity.getStandardTime())
						.host(routerEntity.getHost())
						.certification(routerEntity.getCertification())
						.password(routerEntity.getPassword())
						.build());
	}
}
