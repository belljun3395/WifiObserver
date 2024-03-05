package com.observer.domain.service.member.support;

import com.observer.domain.external.dao.member.MemberDao;
import com.observer.entity.member.MemberEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetMemberInfoSupportService {

	private final MemberDao memberDao;

	public Optional<MemberInfoSupport> execute(String apiKey) {
		Optional<MemberEntity> memberEntitySource = memberDao.findByApiKeyAndDeletedFalse(apiKey);
		if (memberEntitySource.isEmpty()) {
			return Optional.empty();
		}
		MemberEntity memberEntity = memberEntitySource.get();
		return Optional.of(
				MemberInfoSupport.builder()
						.memberId(memberEntity.getId())
						.status(memberEntity.getStatus())
						.resource(memberEntity.getResource())
						.build());
	}

	public Optional<MemberInfoSupport> execute(Long memberId) {
		Optional<MemberEntity> memberEntitySource = memberDao.findByIdAndDeletedFalse(memberId);
		if (memberEntitySource.isEmpty()) {
			return Optional.empty();
		}
		MemberEntity memberEntity = memberEntitySource.get();
		return Optional.of(
				MemberInfoSupport.builder()
						.memberId(memberEntity.getId())
						.status(memberEntity.getStatus())
						.resource(memberEntity.getResource())
						.build());
	}
}