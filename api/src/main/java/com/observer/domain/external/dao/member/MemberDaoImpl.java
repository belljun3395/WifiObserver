package com.observer.domain.external.dao.member;

import com.observer.entity.member.CertificationData;
import com.observer.entity.member.MemberEntity;
import com.observer.persistence.member.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberDaoImpl implements MemberDao {

	private final MemberRepository memberRepository;

	@Override
	public MemberEntity saveMember(MemberEntity memberEntity) {
		return memberRepository.save(memberEntity);
	}

	@Override
	public boolean existsByCertificationAndDeletedFalse(CertificationData certificationData) {
		return memberRepository.existsByCertificationAndDeletedFalse(certificationData);
	}

	@Override
	public Optional<MemberEntity> findByCertificationAndDeletedFalse(
			CertificationData certificationData) {
		return memberRepository.findByCertificationAndDeletedFalse(certificationData);
	}

	@Override
	public boolean existsByApiKey(String apiKey) {
		return memberRepository.existsByApiKey(apiKey);
	}

	@Override
	public Optional<MemberEntity> findByApiKeyAndDeletedFalse(String apiKey) {
		return memberRepository.findByApiKeyAndDeletedFalse(apiKey);
	}

	@Override
	public void delete(MemberEntity memberEntity) {
		memberRepository.delete(memberEntity);
	}

	@Override
	public Optional<MemberEntity> findByIdAndDeletedFalse(Long memberId) {
		return memberRepository.findByIdAndDeletedFalse(memberId);
	}
}
