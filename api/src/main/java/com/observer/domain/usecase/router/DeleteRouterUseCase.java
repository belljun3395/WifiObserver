package com.observer.domain.usecase.router;

import com.observer.domain.dto.router.DeleteRouterUseCaseRequest;
import com.observer.domain.external.dao.router.RouterDao;
import com.observer.domain.service.router.GetMemberIdService;
import com.observer.entity.router.RouterEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteRouterUseCase {

	private final RouterDao routerDao;

	private final GetMemberIdService getMemberIdService;

	public void execute(DeleteRouterUseCaseRequest request) {
		final String apiKey = request.getApiKey();
		final Long routerId = request.getRouterId();

		log.debug("Get memberId. apiKey: {}", apiKey);
		Long memberId = getMemberIdService.execute(apiKey);

		log.debug("Get router. memberId: {}, routerId: {}", memberId, routerId);
		Optional<RouterEntity> routerEntitySource =
				routerDao.findByIdAndMemberIdAndDeletedFalse(memberId, routerId);
		if (routerEntitySource.isEmpty()) {
			throw new IllegalStateException(
					"Router not found. memberId: " + memberId + ", routerId: " + routerId);
		}
		RouterEntity routerEntity = routerEntitySource.get();

		log.debug("Delete router. memberId: {}, routerId: {}", memberId, routerId);
		routerDao.deleteRouter(routerEntity);
	}
}
