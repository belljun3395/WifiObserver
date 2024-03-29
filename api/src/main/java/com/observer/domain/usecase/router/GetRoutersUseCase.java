package com.observer.domain.usecase.router;

import com.observer.data.entity.router.RouterEntity;
import com.observer.domain.dto.router.GetRoutersUseCaseRequest;
import com.observer.domain.dto.router.GetRoutersUseCaseResponse;
import com.observer.domain.external.dao.router.RouterDao;
import com.observer.domain.service.router.GetRouterMemberIdQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetRoutersUseCase {

	private final RouterDao routerDao;

	private final GetRouterMemberIdQuery getMemberIdService;

	public GetRoutersUseCaseResponse execute(GetRoutersUseCaseRequest request) {
		final String apiKey = request.getApiKey();

		log.debug("Get memberId. apiKey: {}", apiKey);
		Long memberId = getMemberIdService.execute(apiKey);

		log.debug("Get routers. memberId: {}", memberId);
		List<RouterEntity> routerEntities = routerDao.findAllByMemberIdAndDeletedFalse(memberId);

		return GetRoutersUseCaseResponse.from(routerEntities);
	}
}
