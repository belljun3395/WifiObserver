package com.wifi.obs.app.web.controller.v1;

import com.wifi.obs.app.domain.dto.response.device.DeviceInfos;
import com.wifi.obs.app.domain.dto.response.device.DeviceOnConnectInfo;
import com.wifi.obs.app.domain.dto.response.device.DeviceStetInfo;
import com.wifi.obs.app.domain.usecase.device.DeleteDeviceUseCase;
import com.wifi.obs.app.domain.usecase.device.GetDeviceStetFacadeUseCase;
import com.wifi.obs.app.domain.usecase.device.GetOnConnectDeviceFacadeUseCase;
import com.wifi.obs.app.domain.usecase.device.GetServiceDeviceUseCase;
import com.wifi.obs.app.domain.usecase.device.PatchDeviceUseCase;
import com.wifi.obs.app.domain.usecase.device.SaveDeviceUseCase;
import com.wifi.obs.app.support.ApiResponse;
import com.wifi.obs.app.support.ApiResponseGenerator;
import com.wifi.obs.app.support.context.MemberIdAuditHolder;
import com.wifi.obs.app.web.dto.request.StetType;
import com.wifi.obs.app.web.dto.request.device.DeleteDeviceRequest;
import com.wifi.obs.app.web.dto.request.device.PatchDeviceRequest;
import com.wifi.obs.app.web.dto.request.device.SaveDeviceRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/device")
@RequiredArgsConstructor
public class DeviceController {

	private final SaveDeviceUseCase saveDeviceUseCase;
	private final PatchDeviceUseCase patchDeviceUseCase;
	private final DeleteDeviceUseCase deleteDeviceUseCase;
	private final GetServiceDeviceUseCase getServiceDeviceUseCase;
	private final GetOnConnectDeviceFacadeUseCase getOnConnectDeviceFacadeUseCase;
	private final GetDeviceStetFacadeUseCase getDeviceStetFacadeUseCase;

	@PostMapping
	public ApiResponse<ApiResponse.SuccessBody<Void>> save(
			@RequestBody @Valid SaveDeviceRequest request) {
		Long memberId = MemberIdAuditHolder.get();
		saveDeviceUseCase.execute(memberId, request);
		return ApiResponseGenerator.success(HttpStatus.CREATED);
	}

	@PatchMapping
	public ApiResponse<ApiResponse.SuccessBody<Void>> edit(
			@RequestBody @Valid PatchDeviceRequest request) {
		Long memberId = MemberIdAuditHolder.get();
		patchDeviceUseCase.execute(memberId, request);
		return ApiResponseGenerator.success(HttpStatus.CREATED);
	}

	@DeleteMapping
	public ApiResponse<ApiResponse.SuccessBody<Void>> delete(
			@RequestBody @Valid DeleteDeviceRequest request) {
		Long memberId = MemberIdAuditHolder.get();
		deleteDeviceUseCase.execute(memberId, request);
		return ApiResponseGenerator.success(HttpStatus.NO_CONTENT);
	}

	@GetMapping
	public ApiResponse<ApiResponse.SuccessBody<DeviceInfos>> get() {
		Long memberId = MemberIdAuditHolder.get();
		DeviceInfos res = getServiceDeviceUseCase.execute(memberId);
		return ApiResponseGenerator.success(res, HttpStatus.OK);
	}

	@GetMapping("/service/{serviceId}")
	public ApiResponse<ApiResponse.SuccessBody<DeviceInfos>> browseServiceDevice(
			@PathVariable Long serviceId) {
		Long memberId = MemberIdAuditHolder.get();
		DeviceInfos res = getServiceDeviceUseCase.execute(memberId, serviceId);
		return ApiResponseGenerator.success(res, HttpStatus.OK);
	}

	@GetMapping("/connect")
	public ApiResponse<ApiResponse.SuccessBody<DeviceOnConnectInfo>> isConnect(
			@RequestParam("mac") String mac) {
		Long memberId = MemberIdAuditHolder.get();
		DeviceOnConnectInfo res = getOnConnectDeviceFacadeUseCase.execute(memberId, mac);
		return ApiResponseGenerator.success(res, HttpStatus.OK);
	}

	@GetMapping(value = "/statistics")
	public ApiResponse<ApiResponse.SuccessBody<DeviceStetInfo>> deviceStet(
			@RequestParam("mac") String mac, @RequestParam("type") StetType type) {
		Long memberId = MemberIdAuditHolder.get();
		DeviceStetInfo res = getDeviceStetFacadeUseCase.execute(memberId, mac, type);
		return ApiResponseGenerator.success(res, HttpStatus.OK);
	}
}
