package com.wifi.obs.app.domain.usecase.member;

import com.wifi.obs.app.domain.converter.MemberConverter;
import com.wifi.obs.app.domain.dto.response.member.SavedMemberInfo;
import com.wifi.obs.app.domain.model.member.Member;
import com.wifi.obs.app.exception.domain.NotMatchPasswordException;
import com.wifi.obs.app.security.authentication.authority.Roles;
import com.wifi.obs.app.support.token.TokenGenerator;
import com.wifi.obs.app.web.dto.request.member.SaveMemberRequest;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.repository.member.MemberRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveMemberUseCase {

	private static final Long NOT_EXIST_MEMBER = -1L;

	private final MemberRepository memberRepository;
	private final TokenGenerator tokenGenerator;

	private final MemberConverter memberConverter;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME)
	public SavedMemberInfo execute(SaveMemberRequest request) {

		Long existMember = isExistMember(request.getEmail());

		if (!Objects.equals(existMember, NOT_EXIST_MEMBER)) {
			Member member = getMember(existMember);

			validatePassword(member, request.getPassword());

			return getSavedMember(member.getId());
		}

		Long savedMemberId = saveMember(request);

		return getSavedMember(savedMemberId);
	}

	private Long isExistMember(String email) {
		Optional<MemberEntity> source = memberRepository.findByCertificationAndDeletedFalse(email);

		if (source.isPresent()) {
			return source.get().getId();
		}

		return NOT_EXIST_MEMBER;
	}

	private Member getMember(Long existMember) {
		return memberConverter.from(
				Objects.requireNonNull(memberRepository.findByIdAndDeletedFalse(existMember).orElse(null)));
	}

	private void validatePassword(Member member, String requestPassword) {
		if (!member.isPassword(requestPassword)) {
			throw new NotMatchPasswordException();
		}
	}

	private Long saveMember(SaveMemberRequest request) {
		return memberRepository
				.save(
						MemberEntity.builder()
								.certification(request.getEmail())
								.password(request.getPassword())
								.build())
				.getId();
	}

	private SavedMemberInfo getSavedMember(Long memberId) {
		return SavedMemberInfo.builder()
				.id(memberId)
				.token(tokenGenerator.generateAuthToken(memberId, List.of(Roles.USER)))
				.build();
	}
}
