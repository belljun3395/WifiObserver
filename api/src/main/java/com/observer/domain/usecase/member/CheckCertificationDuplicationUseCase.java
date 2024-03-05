package com.observer.domain.usecase.member;

import com.observer.domain.dto.member.CheckCertificationDuplicationUseCaseRequest;
import com.observer.domain.dto.member.CheckCertificationDuplicationUseCaseResponse;
import com.observer.domain.external.dao.member.MemberDao;
import com.observer.entity.member.CertificationData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckCertificationDuplicationUseCase {

	private final MemberDao memberDao;

	@Transactional
	public CheckCertificationDuplicationUseCaseResponse execute(
			CheckCertificationDuplicationUseCaseRequest certification) {
		final CertificationData certificationData =
				CertificationData.builder().certification(certification.getCertification()).build();

		log.debug("Check duplication. certification: {}", certification.getCertification());
		boolean isDuplicateCertification =
				memberDao.existsByCertificationAndDeletedFalse(certificationData);
		if (isDuplicateCertification) {
			log.debug("Certification is duplicated. certification: {}", certification.getCertification());
			return CheckCertificationDuplicationUseCaseResponse.builder().duplication(true).build();
		}
		log.debug(
				"Certification is not duplicated. certification: {}", certification.getCertification());
		return CheckCertificationDuplicationUseCaseResponse.builder().duplication(false).build();
	}
}
