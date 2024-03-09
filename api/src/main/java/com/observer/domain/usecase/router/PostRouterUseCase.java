package com.observer.domain.usecase.router;

import com.observer.data.entity.router.RouterEntity;
import com.observer.data.entity.router.RouterServiceType;
import com.observer.domain.dto.router.PostRouterUseCaseRequest;
import com.observer.domain.dto.router.PostRouterUseCaseResponse;
import com.observer.domain.external.dao.router.RouterDao;
import com.observer.domain.service.router.CheckMemberPostRouter;
import com.observer.domain.service.router.GetRouterMemberIdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostRouterUseCase {

	private final RouterDao routerDao;

	private final GetRouterMemberIdService getMemberIdService;

	private final CheckMemberPostRouter checkMemberPostRouter;

	public PostRouterUseCaseResponse execute(PostRouterUseCaseRequest request) {
		final String apiKey = request.getApiKey();
		final RouterServiceType serviceType = RouterServiceType.valueOf(request.getServiceType());
		final String host = request.getHost();
		final String certification = request.getCertification();
		final String password = request.getPassword();

		log.debug("Get memberId. apiKey: {}", apiKey);
		Long memberId = getMemberIdService.execute(apiKey);

		log.debug("Check member can post router. memberId: {}", memberId);
		boolean canPost = checkMemberPostRouter.execute(memberId);
		if (!canPost) {
			throw new IllegalStateException("member can't post more router");
		}

		RouterEntity routerEntity =
				routerDao.saveRouter(
						RouterEntity.builder()
								.memberId(memberId)
								.serviceType(serviceType)
								.host(host)
								.certification(certification)
								.password(password)
								.build());
		log.debug("Post router. memberId: {}, routerId: {}", memberId, routerEntity.getId());

		return PostRouterUseCaseResponse.builder()
				.routerId(routerEntity.getId())
				.serviceType(routerEntity.getServiceType().getType())
				.host(routerEntity.getHost())
				.build();
	}
}
