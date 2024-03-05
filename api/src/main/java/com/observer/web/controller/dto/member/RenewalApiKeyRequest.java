package com.observer.web.controller.dto.member;

import com.observer.web.controller.dto.validator.Certification;
import com.observer.web.controller.dto.validator.Password;
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
public class RenewalApiKeyRequest {

	@Certification private String certification;
	@Password private String password;
}
