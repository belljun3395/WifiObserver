package com.observer.web.controller.connection;

import com.observer.domain.dto.connection.GetConnectionHistoryUseCaseRequest;
import com.observer.domain.dto.connection.GetConnectionHistoryUseCaseResponse;
import com.observer.domain.dto.connection.IptimeCurrentConnectionUseCaseRequest;
import com.observer.domain.dto.connection.IptimeCurrentConnectionUseCaseResponse;
import com.observer.domain.usecase.connection.GetConnectionHistoryUseCase;
import com.observer.domain.usecase.connection.GetIptimeConnectionUsersUseCase;
import com.observer.security.authentication.api.ApiKeyUserDetails;
import com.observer.web.support.ApiResponse;
import com.observer.web.support.ApiResponseGenerator;
import com.observer.web.support.MessageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/connections")
@RequiredArgsConstructor
public class ConnectionController {

	private final GetConnectionHistoryUseCase getConnectionHistoryUseCase;
	private final GetIptimeConnectionUsersUseCase getIptimeConnectionUsersUseCase;

	@GetMapping("/history/{routerId}")
	public ApiResponse<ApiResponse.SuccessBody<GetConnectionHistoryUseCaseResponse>> history(
			@AuthenticationPrincipal ApiKeyUserDetails userDetails,
			@PathVariable(value = "routerId") Long routerId) {
		GetConnectionHistoryUseCaseRequest request =
				GetConnectionHistoryUseCaseRequest.builder()
						.apiKey(userDetails.getApiKeyValue())
						.routerId(routerId)
						.build();
		GetConnectionHistoryUseCaseResponse response = getConnectionHistoryUseCase.execute(request);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/iptime/{routerId}")
	public ApiResponse<ApiResponse.SuccessBody<IptimeCurrentConnectionUseCaseResponse>> iptimeConnect(
			@AuthenticationPrincipal ApiKeyUserDetails userDetails,
			@PathVariable(value = "routerId") Long routerId) {
		IptimeCurrentConnectionUseCaseRequest request =
				IptimeCurrentConnectionUseCaseRequest.builder()
						.apiKey(userDetails.getApiKeyValue())
						.routerId(routerId)
						.build();
		IptimeCurrentConnectionUseCaseResponse response =
				getIptimeConnectionUsersUseCase.execute(request);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}
}
