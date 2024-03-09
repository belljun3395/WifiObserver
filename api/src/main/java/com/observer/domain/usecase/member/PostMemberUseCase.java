package com.observer.domain.usecase.member;

import com.observer.data.entity.member.CertificationData;
import com.observer.data.entity.member.MemberEntity;
import com.observer.data.entity.member.MemberPasswordData;
import com.observer.domain.dto.member.PostMemberUseCaseRequest;
import com.observer.domain.dto.member.PostMemberUseCaseResponse;
import com.observer.domain.external.dao.member.MemberDao;
import com.observer.domain.service.member.ApiKeyGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostMemberUseCase {

	private final MemberDao memberDao;

	private final ApiKeyGenerateService apiKeyGenerateService;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	public PostMemberUseCaseResponse execute(PostMemberUseCaseRequest request) {
		final CertificationData certification =
				CertificationData.builder().certification(request.getCertification()).build();
		MemberPasswordData password =
				MemberPasswordData.builder().password(request.getPassword()).build();

		log.debug("Check duplicate certification. certification: {}", certification.getCertification());
		boolean isDuplicateId = memberDao.existsByCertificationAndDeletedFalse(certification);
		if (isDuplicateId) {
			throw new IllegalStateException(
					"Certification is duplicated. certification: " + certification.getCertification());
		}

		password = encodePassword(password);

		String apiKey = apiKeyGenerateService.generate();

		// todo certification을 기준으로 락을 걸어 처리 해야 함
		MemberEntity memberSource =
				MemberEntity.builder()
						.certification(certification)
						.password(password)
						.apiKey(apiKey)
						.build();
		MemberEntity memberEntity = memberDao.saveMember(memberSource);
		log.debug("Save member. memberSource: {}", memberSource);

		return PostMemberUseCaseResponse.builder()
				.memberId(memberEntity.getId())
				.certification(memberEntity.getCertification().getCertification())
				.password(memberEntity.getPassword().getPassword())
				.apiKey(memberEntity.getApiKey())
				.build();
	}

	private MemberPasswordData encodePassword(MemberPasswordData password) {
		String encodedPassword = passwordEncoder.encode(password.getPassword());
		return password.toBuilder().password(encodedPassword).build();
	}
}
