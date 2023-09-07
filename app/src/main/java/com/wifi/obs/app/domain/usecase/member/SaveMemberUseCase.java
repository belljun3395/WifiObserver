package com.wifi.obs.app.domain.usecase.member;

import com.wifi.obs.app.domain.converter.MemberModelConverter;
import com.wifi.obs.app.domain.dto.response.member.SavedMemberInfo;
import com.wifi.obs.app.domain.model.MemberModel;
import com.wifi.obs.app.exception.domain.NotMatchPasswordException;
import com.wifi.obs.app.security.authentication.authority.Roles;
import com.wifi.obs.app.support.token.TokenGenerator;
import com.wifi.obs.app.web.dto.request.member.SaveMemberRequest;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.repository.member.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveMemberUseCase {

	private final MemberRepository memberRepository;
	private final TokenGenerator tokenGenerator;

	private final MemberModelConverter memberModelConverter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public SavedMemberInfo execute(SaveMemberRequest request) {

		Optional<MemberEntity> memberRecord = getMember(request);

		if (memberRecord.isPresent()) {
			MemberModel member = memberModelConverter.from(memberRecord.get());

			validateRequestPassword(request.getPassword(), member);

			return getSavedMember(member.getId());
		}

		Long savedMemberId =
				memberRepository
						.save(
								MemberEntity.builder()
										.certification(request.getEmail())
										.password(request.getPassword())
										.build())
						.getId();

		return getSavedMember(savedMemberId);
	}

	private Optional<MemberEntity> getMember(SaveMemberRequest request) {
		return memberRepository.findByCertificationAndDeletedFalse(request.getEmail());
	}

	private void validateRequestPassword(String requestPassword, MemberModel member) {
		if (!member.isSamePassword(requestPassword)) {
			throw new NotMatchPasswordException();
		}
	}

	private SavedMemberInfo getSavedMember(Long memberId) {
		return SavedMemberInfo.builder()
				.id(memberId)
				.authToken(tokenGenerator.generateAuthToken(memberId, List.of(Roles.USER)))
				.build();
	}
}
