package com.observer.data.persistence.router;

import com.observer.data.entity.router.RouterEntity;
import com.observer.data.entity.router.RouterStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouterRepository extends JpaRepository<RouterEntity, Long> {

	boolean existsByMemberIdAndDeletedFalse(Long memberId);

	List<RouterEntity> findAllByMemberIdAndDeletedFalse(Long memberId);

	Optional<RouterEntity> findByIdAndMemberIdAndDeletedFalse(Long id, Long memberId);

	Optional<RouterEntity> findByIdAndStatusAndDeletedFalse(Long id, RouterStatus status);

	Optional<RouterEntity> findByHostAndDeletedFalse(String host);
}
