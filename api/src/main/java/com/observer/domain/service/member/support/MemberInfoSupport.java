package com.observer.domain.service.member.support;

import com.observer.data.entity.member.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MemberInfoSupport {

	private Long memberId;
	private MemberStatus status;
	private String resource;
}
