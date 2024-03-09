package com.observer.web.controller.validator.sample;

import com.observer.web.controller.dto.sample.SampleBrowseRequest;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SampleBrowseRequestValidator implements Validator {

	private static final Pattern PATTERN_HOST =
			Pattern.compile(
					"(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}");

	private static final List<String> NOT_ALLOW_HOST_FIRST_PART = List.of("192", "10", "172");

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(SampleBrowseRequest.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		SampleBrowseRequest request = (SampleBrowseRequest) target;
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

		String hostFirstPart = StringUtils.substringBefore(request.getHost(), ".");
		if (NOT_ALLOW_HOST_FIRST_PART.contains(hostFirstPart)) {
			errors.rejectValue("host", "host.invalid");
		}
	}
}
