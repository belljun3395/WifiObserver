package com.observer.domain.dto.router;

import com.observer.entity.router.RouterEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class GetRoutersUseCaseResponse {

	List<Router> routers;

	public static GetRoutersUseCaseResponse from(List<RouterEntity> routerEntities) {
		List<Router> routerSources =
				routerEntities.stream()
						.map(
								routerEntity ->
										Router.builder()
												.routerId(routerEntity.getId())
												.serviceType(routerEntity.getServiceType().getType())
												.host(routerEntity.getHost())
												.build())
						.collect(Collectors.toList());
		return new GetRoutersUseCaseResponse(routerSources);
	}

	@Getter
	@ToString
	@EqualsAndHashCode
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder(toBuilder = true)
	private static class Router {
		private Long routerId;
		private String serviceType;
		private String host;
	}
}
