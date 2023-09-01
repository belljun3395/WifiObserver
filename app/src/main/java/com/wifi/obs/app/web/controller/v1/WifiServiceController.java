package com.wifi.obs.app.web.controller.v1;

import com.wifi.obs.app.domain.dto.response.service.OnConnectUserInfos;
import com.wifi.obs.app.domain.dto.response.service.ServiceDeviceStetInfos;
import com.wifi.obs.app.domain.dto.response.service.WifiServiceInfos;
import com.wifi.obs.app.domain.usecase.wifiService.DeleteWifiServiceUseCase;
import com.wifi.obs.app.domain.usecase.wifiService.GetHealthStatusUseCase;
import com.wifi.obs.app.domain.usecase.wifiService.GetServiceStetFacadeUseCase;
import com.wifi.obs.app.domain.usecase.wifiService.GetUsersFacadeUseCase;
import com.wifi.obs.app.domain.usecase.wifiService.GetWifiServiceInfoUseCase;
import com.wifi.obs.app.domain.usecase.wifiService.SaveWifiServiceUseCase;
import com.wifi.obs.app.support.ApiResponse;
import com.wifi.obs.app.support.ApiResponseGenerator;
import com.wifi.obs.app.support.context.MemberIdAuditHolder;
import com.wifi.obs.app.web.dto.request.StetType;
import com.wifi.obs.app.web.dto.request.service.DeleteServiceRequest;
import com.wifi.obs.app.web.dto.request.service.SaveServiceRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wifi")
@RequiredArgsConstructor
public class WifiServiceController {

	private final SaveWifiServiceUseCase saveWifiServiceUseCase;
	private final DeleteWifiServiceUseCase deleteWifiServiceUseCase;
	private final GetWifiServiceInfoUseCase getWifiServiceInfoUseCase;
	private final GetUsersFacadeUseCase getUsersFacadeUseCase;
	private final GetHealthStatusUseCase getHealthStatusUseCase;
	private final GetServiceStetFacadeUseCase getServiceStetFacadeUseCase;

	@PostMapping
	public ApiResponse<ApiResponse.SuccessBody<Void>> save(@RequestBody SaveServiceRequest request) {
		Long memberId = MemberIdAuditHolder.get();
		saveWifiServiceUseCase.execute(memberId, request);
		return ApiResponseGenerator.success(HttpStatus.CREATED);
	}

	@DeleteMapping
	public ApiResponse<ApiResponse.SuccessBody<Void>> delete(
			@RequestBody DeleteServiceRequest request) {
		Long memberId = MemberIdAuditHolder.get();
		deleteWifiServiceUseCase.execute(memberId, request);
		return ApiResponseGenerator.success(HttpStatus.OK);
	}

	@GetMapping("/service")
	public ApiResponse<ApiResponse.SuccessBody<WifiServiceInfos>> broseServiceInfo() {
		Long memberId = MemberIdAuditHolder.get();
		WifiServiceInfos res = getWifiServiceInfoUseCase.execute(memberId);
		return ApiResponseGenerator.success(res, HttpStatus.OK);
	}

	@GetMapping(value = "/users")
	public ApiResponse<ApiResponse.SuccessBody<OnConnectUserInfos>> browseUsers(
			@RequestParam("sid") Long sid, @RequestParam(value = "ft", required = false) Boolean ft) {
		Long memberId = MemberIdAuditHolder.get();
		OnConnectUserInfos res = getUsersFacadeUseCase.execute(memberId, sid, Optional.ofNullable(ft));

		return ApiResponseGenerator.success(res, HttpStatus.OK);
	}

	@GetMapping(value = "/health")
	public ApiResponse<ApiResponse.SuccessBody<HttpStatus>> health(@RequestParam("sid") Long sid) {
		Long memberId = MemberIdAuditHolder.get();
		HttpStatus res = getHealthStatusUseCase.execute(memberId, sid);
		return ApiResponseGenerator.success(res, HttpStatus.OK);
	}

	@GetMapping(value = "/statistics")
	public ApiResponse<ApiResponse.SuccessBody<ServiceDeviceStetInfos>> serviceStet(
			@RequestParam("sid") Long sid, @RequestParam("type") StetType type) {
		Long memberId = MemberIdAuditHolder.get();
		ServiceDeviceStetInfos res = getServiceStetFacadeUseCase.execute(memberId, sid, type);
		return ApiResponseGenerator.success(res, HttpStatus.OK);
	}
}
