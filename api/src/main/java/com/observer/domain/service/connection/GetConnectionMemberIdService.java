package com.observer.domain.service.connection;

import com.observer.domain.service.member.support.GetMemberInfoSupportService;
import com.observer.domain.service.member.support.MemberInfoSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetConnectionMemberIdService {

	private final GetMemberInfoSupportService getMemberInfoSupportService;

	public Long execute(String apiKey) {
		MemberInfoSupport memberInfoSupport =
				getMemberInfoSupportService.execute(apiKey).orElseThrow(IllegalStateException::new);
		return memberInfoSupport.getMemberId();
	}
}
