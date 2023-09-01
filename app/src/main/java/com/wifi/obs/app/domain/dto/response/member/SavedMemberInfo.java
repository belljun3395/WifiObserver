package com.wifi.obs.app.domain.dto.response.member;

import com.wifi.obs.app.support.token.AuthToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SavedMemberInfo {

	private Long id;
	private AuthToken authToken;
}
