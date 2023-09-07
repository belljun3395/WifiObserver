package com.wifi.obs.app.domain.usecase.util.validator;

import com.wifi.obs.app.exception.domain.NotMatchInformationException;
import org.springframework.stereotype.Component;

@Component
public class IdMatchValidator {

	public void validate(Long source, Long target) {
		if (!source.equals(target)) {
			throw new NotMatchInformationException();
		}
	}
}
