package com.observer.domain.usecase.member;

import com.observer.domain.dto.member.DeleteMemberUseCaseRequest;
import com.observer.domain.external.dao.member.MemberDao;
import com.observer.entity.member.MemberEntity;
import com.observer.entity.member.MemberPasswordData;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteMemberUseCase {

	private final MemberDao memberDao;

	private final PasswordEncoder passwordEncoder;

	public void execute(DeleteMemberUseCaseRequest request) {
		final String apiKey = request.getApiKey();
		MemberPasswordData password =
				MemberPasswordData.builder().password(request.getPassword()).build();

		Optional<MemberEntity> memberEntitySource = memberDao.findByApiKeyAndDeletedFalse(apiKey);
		if (memberEntitySource.isEmpty()) {
			log.error("Member is not found. apiKey: {}", apiKey);
			return;
		}
		MemberEntity memberEntity = memberEntitySource.get();

		MemberPasswordData entityPassword = memberEntity.getPassword();
		boolean isMatchPassword =
				passwordEncoder.matches(password.getPassword(), entityPassword.getPassword());
		if (isMatchPassword) {
			throw new IllegalArgumentException("Password is not matched.");
		}

		memberDao.delete(memberEntity);
	}
}
