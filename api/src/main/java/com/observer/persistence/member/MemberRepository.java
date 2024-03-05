package com.observer.persistence.member;

import com.observer.entity.member.CertificationData;
import com.observer.entity.member.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

	boolean existsByCertificationAndDeletedFalse(CertificationData certification);

	Optional<MemberEntity> findByCertificationAndDeletedFalse(CertificationData certification);

	boolean existsByApiKey(String apiKey);

	Optional<MemberEntity> findByApiKeyAndDeletedFalse(String apiKey);

	Optional<MemberEntity> findByIdAndDeletedFalse(Long memberId);
}
