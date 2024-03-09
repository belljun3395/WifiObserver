package com.observer.domain.external.dao.router;

import com.observer.data.entity.router.RouterEntity;
import com.observer.data.entity.router.RouterStatus;
import com.observer.data.persistence.router.RouterRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RouterDaoImpl implements RouterDao {

	private final RouterRepository routerRepository;

	@Override
	public boolean existsByMemberIdAndDeletedFalse(Long memberId) {
		return routerRepository.existsByMemberIdAndDeletedFalse(memberId);
	}

	@Override
	public List<RouterEntity> findAllByMemberIdAndDeletedFalse(Long memberId) {
		return routerRepository.findAllByMemberIdAndDeletedFalse(memberId);
	}

	@Override
	public Optional<RouterEntity> findByIdAndMemberIdAndDeletedFalse(Long id, Long memberId) {
		return routerRepository.findByIdAndMemberIdAndDeletedFalse(id, memberId);
	}

	@Override
	public void deleteRouter(RouterEntity routerEntity) {
		routerRepository.delete(routerEntity);
	}

	@Override
	public RouterEntity saveRouter(RouterEntity routerEntity) {
		return routerRepository.save(routerEntity);
	}

	@Override
	public Optional<RouterEntity> findByIdAndStatusAndDeletedFalse(Long id, RouterStatus status) {
		return routerRepository.findByIdAndStatusAndDeletedFalse(id, status);
	}
}
