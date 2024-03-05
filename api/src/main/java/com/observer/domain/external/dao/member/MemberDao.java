package com.observer.domain.external.dao.member;

import com.observer.entity.member.CertificationData;
import com.observer.entity.member.MemberEntity;
import java.util.Optional;

public interface MemberDao {

	MemberEntity saveMember(MemberEntity memberEntity);

	boolean existsByCertificationAndDeletedFalse(CertificationData certificationData);

	Optional<MemberEntity> findByCertificationAndDeletedFalse(CertificationData certificationData);

	boolean existsByApiKey(String apiKey);

	Optional<MemberEntity> findByApiKeyAndDeletedFalse(String apiKey);

	void delete(MemberEntity memberEntity);

	Optional<MemberEntity> findByIdAndDeletedFalse(Long memberId);
}
