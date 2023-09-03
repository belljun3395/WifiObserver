package com.wifi.obs.app.domain.service.member;

import com.wifi.obs.app.exception.domain.MemberNotFoundException;
import com.wifi.obs.data.mysql.config.JpaDataSourceConfig;
import com.wifi.obs.data.mysql.entity.member.MemberEntity;
import com.wifi.obs.data.mysql.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidatedMemberService {

	private final MemberRepository memberRepository;

	@Transactional(transactionManager = JpaDataSourceConfig.TRANSACTION_MANAGER_NAME, readOnly = true)
	public MemberEntity execute(Long memberId) {
		return memberRepository
				.findById(memberId)
				.orElseThrow(() -> new MemberNotFoundException(memberId));
	}
}
