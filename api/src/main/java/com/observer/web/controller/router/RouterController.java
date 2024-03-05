package com.observer.web.controller.router;

import com.observer.domain.dto.router.DeleteRouterUseCaseRequest;
import com.observer.domain.dto.router.GetRoutersUseCaseRequest;
import com.observer.domain.dto.router.GetRoutersUseCaseResponse;
import com.observer.domain.dto.router.PostRouterUseCaseRequest;
import com.observer.domain.dto.router.PostRouterUseCaseResponse;
import com.observer.domain.usecase.router.DeleteRouterUseCase;
import com.observer.domain.usecase.router.GetRoutersUseCase;
import com.observer.domain.usecase.router.PostRouterUseCase;
import com.observer.security.authentication.api.ApiKeyUserDetails;
import com.observer.web.controller.dto.router.DeleteRouterRequest;
import com.observer.web.controller.dto.router.PostRouterRequest;
import com.observer.web.support.ApiResponse;
import com.observer.web.support.ApiResponseGenerator;
import com.observer.web.support.MessageCode;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/routers")
@RequiredArgsConstructor
public class RouterController {

	private final PostRouterUseCase postRouterUseCase;
	private final DeleteRouterUseCase deleteRouterUseCase;
	private final GetRoutersUseCase getRoutersUseCase;

	@PostMapping()
	public ApiResponse<ApiResponse.SuccessBody<PostRouterUseCaseResponse>> save(
			@AuthenticationPrincipal ApiKeyUserDetails userDetails,
			@Valid @RequestBody PostRouterRequest request) {
		PostRouterUseCaseRequest useCaseRequest =
				PostRouterUseCaseRequest.builder()
						.apiKey(userDetails.getApiKeyValue())
						.host(request.getHost())
						.certification(request.getCertification())
						.password(request.getPassword())
						.build();
		PostRouterUseCaseResponse response = postRouterUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.RESOURCE_CREATED);
	}

	@PostMapping("/delete")
	public ApiResponse<ApiResponse.Success> delete(
			@AuthenticationPrincipal ApiKeyUserDetails userDetails,
			@Valid @RequestBody DeleteRouterRequest request) {
		DeleteRouterUseCaseRequest useCaseRequest =
				DeleteRouterUseCaseRequest.builder()
						.apiKey(userDetails.getApiKeyValue())
						.routerId(request.getRouterId())
						.build();
		deleteRouterUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_DELETED);
	}

	@GetMapping
	public ApiResponse<ApiResponse.SuccessBody<GetRoutersUseCaseResponse>> browse(
			@AuthenticationPrincipal ApiKeyUserDetails userDetails) {
		GetRoutersUseCaseRequest useCaseRequest =
				GetRoutersUseCaseRequest.builder().apiKey(userDetails.getApiKeyValue()).build();
		GetRoutersUseCaseResponse response = getRoutersUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}
}
