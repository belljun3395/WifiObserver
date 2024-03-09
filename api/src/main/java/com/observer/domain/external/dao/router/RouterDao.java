package com.observer.domain.external.dao.router;

import com.observer.data.entity.router.RouterEntity;
import com.observer.data.entity.router.RouterStatus;
import java.util.List;
import java.util.Optional;

public interface RouterDao {
	boolean existsByMemberIdAndDeletedFalse(Long memberId);

	List<RouterEntity> findAllByMemberIdAndDeletedFalse(Long memberId);

	Optional<RouterEntity> findByIdAndMemberIdAndDeletedFalse(Long id, Long memberId);

	void deleteRouter(RouterEntity routerEntity);

	RouterEntity saveRouter(RouterEntity routerEntity);

	Optional<RouterEntity> findByIdAndStatusAndDeletedFalse(Long id, RouterStatus status);
}
