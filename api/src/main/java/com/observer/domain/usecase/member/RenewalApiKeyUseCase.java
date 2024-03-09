package com.observer.domain.usecase.member;

import com.observer.data.entity.member.CertificationData;
import com.observer.data.entity.member.MemberEntity;
import com.observer.data.entity.member.MemberPasswordData;
import com.observer.domain.dto.member.RenewalApiKeyUseCaseRequest;
import com.observer.domain.dto.member.RenewalApiKeyUseCaseResponse;
import com.observer.domain.external.dao.member.MemberDao;
import com.observer.domain.service.member.ApiKeyGenerateService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RenewalApiKeyUseCase {

	private final MemberDao memberDao;

	private final ApiKeyGenerateService apiKeyGenerateService;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	public RenewalApiKeyUseCaseResponse execute(RenewalApiKeyUseCaseRequest request) {
		final CertificationData certification =
				CertificationData.builder().certification(request.getCertification()).build();
		MemberPasswordData password =
				MemberPasswordData.builder().password(request.getPassword()).build();

		Optional<MemberEntity> memberEntitySource =
				memberDao.findByCertificationAndDeletedFalse(certification);
		if (memberEntitySource.isEmpty()) {
			throw new IllegalStateException(
					"Member is not found. certification: " + certification.getCertification());
		}

		MemberEntity memberEntity = memberEntitySource.get();

		MemberPasswordData entityPassword = memberEntity.getPassword();
		boolean isMatchPassword =
				passwordEncoder.matches(password.getPassword(), entityPassword.getPassword());
		if (isMatchPassword) {
			throw new IllegalArgumentException("Password is not matched.");
		}

		String apiKey = apiKeyGenerateService.generate();

		memberEntity = memberEntity.toBuilder().apiKey(apiKey).build();
		memberDao.saveMember(memberEntity);

		return RenewalApiKeyUseCaseResponse.builder().apiKey(apiKey).build();
	}
}
