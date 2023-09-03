package com.wifi.obs.app.web.controller.validator.beta;

import com.wifi.obs.app.web.dto.request.beta.IptimeBetaRequest;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class IptimeBetaRequestValidator implements Validator {

	private static final Pattern PATTERN_HOST =
			Pattern.compile(
					"(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}");

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(IptimeBetaRequest.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		IptimeBetaRequest request = (IptimeBetaRequest) target;
		if (request.getHost() == null || request.getHost().isEmpty()) {
			errors.rejectValue("host", "host.empty");
		}
		if (request.getCertification() == null || request.getCertification().isEmpty()) {
			errors.rejectValue("certification", "certification.empty");
		}
		if (request.getPassword() == null || request.getPassword().isEmpty()) {
			errors.rejectValue("password", "password.empty");
		}

		if (request.getPort() == null) {
			errors.rejectValue("port", "port.empty");
		}

		if (!PATTERN_HOST.matcher(request.getHost()).matches()) {
			errors.rejectValue("host", "host.invalid");
		}
	}
}
