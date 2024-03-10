package com.observer.domain.service.connection;

import com.observer.domain.service.member.support.GetMemberInfoSupportQuery;
import com.observer.domain.service.member.support.MemberInfoSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetConnectionMemberIdQuery {

	private final GetMemberInfoSupportQuery getMemberInfoSupportQuery;

	public Long execute(String apiKey) {
		MemberInfoSupport memberInfoSupport =
				getMemberInfoSupportQuery.execute(apiKey).orElseThrow(IllegalStateException::new);
		return memberInfoSupport.getMemberId();
	}
}
