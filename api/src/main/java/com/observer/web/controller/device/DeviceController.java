package com.observer.web.controller.device;

import com.observer.domain.dto.device.DeleteDeviceUseCaseRequest;
import com.observer.domain.dto.device.GetDevicesUseCaseRequest;
import com.observer.domain.dto.device.GetDevicesUseCaseResponse;
import com.observer.domain.dto.device.PostDeviceUseCaseRequest;
import com.observer.domain.dto.device.PostDeviceUseCaseResponse;
import com.observer.domain.usecase.device.DeleteDeviceUseCase;
import com.observer.domain.usecase.device.GetDevicesUseCase;
import com.observer.domain.usecase.device.PostDeviceUseCase;
import com.observer.security.authentication.api.ApiKeyUserDetails;
import com.observer.web.controller.dto.device.DeleteDeviceRequest;
import com.observer.web.controller.dto.device.PostDeviceRequest;
import com.observer.web.support.ApiResponse;
import com.observer.web.support.ApiResponseGenerator;
import com.observer.web.support.MessageCode;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {

	private final PostDeviceUseCase postDeviceUseCase;
	private final DeleteDeviceUseCase deleteDeviceUseCase;
	private final GetDevicesUseCase getDeviceUseCase;

	@PostMapping()
	public ApiResponse<ApiResponse.SuccessBody<PostDeviceUseCaseResponse>> save(
			@AuthenticationPrincipal ApiKeyUserDetails userDetails,
			@Valid @RequestBody PostDeviceRequest request) {
		PostDeviceUseCaseRequest useCaseRequest =
				PostDeviceUseCaseRequest.builder()
						.apiKey(userDetails.getApiKeyValue())
						.routeId(request.getRouteId())
						.mac(request.getMac())
						.info(request.getInfo())
						.build();
		PostDeviceUseCaseResponse response = postDeviceUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PostMapping("/delete")
	public ApiResponse<ApiResponse.Success> delete(
			@AuthenticationPrincipal ApiKeyUserDetails userDetails,
			@Valid @RequestBody DeleteDeviceRequest request) {
		DeleteDeviceUseCaseRequest useCaseRequest =
				DeleteDeviceUseCaseRequest.builder()
						.apiKey(userDetails.getApiKeyValue())
						.routeId(request.getRouteId())
						.deviceId(request.getDeviceId())
						.build();
		deleteDeviceUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_DELETED);
	}

	@GetMapping("/routers/{routerId}")
	public ApiResponse<ApiResponse.SuccessBody<GetDevicesUseCaseResponse>> getRouterDevices(
			@AuthenticationPrincipal ApiKeyUserDetails userDetails,
			@PathVariable(value = "routerId") Long routerId) {
		GetDevicesUseCaseRequest useCaseRequest =
				GetDevicesUseCaseRequest.builder()
						.apiKey(userDetails.getApiKeyValue())
						.routeId(routerId)
						.build();
		GetDevicesUseCaseResponse response = getDeviceUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}
}
