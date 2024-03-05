package com.observer.persistence.router;

import com.observer.entity.router.RouterEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouterRepository extends JpaRepository<RouterEntity, Long> {

	boolean existsByMemberIdAndDeletedFalse(Long memberId);

	List<RouterEntity> findAllByMemberIdAndDeletedFalse(Long memberId);

	Optional<RouterEntity> findByIdAndMemberIdAndDeletedFalse(Long id, Long memberId);
}
