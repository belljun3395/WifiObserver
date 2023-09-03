package com.wifi.obs.app.web.controller.v1;

import com.wifi.obs.app.domain.dto.response.member.SavedMemberInfo;
import com.wifi.obs.app.domain.usecase.member.DeleteMemberUseCase;
import com.wifi.obs.app.domain.usecase.member.SaveMemberUseCase;
import com.wifi.obs.app.support.ApiResponse;
import com.wifi.obs.app.support.ApiResponseGenerator;
import com.wifi.obs.app.support.context.MemberIdAuditHolder;
import com.wifi.obs.app.web.dto.request.member.SaveMemberRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

	private final SaveMemberUseCase saveMemberUseCase;
	private final DeleteMemberUseCase deleteMemberUseCase;

	@PostMapping
	public ApiResponse<ApiResponse.SuccessBody<SavedMemberInfo>> save(
			@RequestBody @Valid SaveMemberRequest request) {
		SavedMemberInfo res = saveMemberUseCase.execute(request);
		return ApiResponseGenerator.success(res, HttpStatus.CREATED);
	}

	@DeleteMapping
	public ApiResponse<ApiResponse.SuccessBody<Void>> delete() {
		Long memberId = MemberIdAuditHolder.get();
		deleteMemberUseCase.execute(memberId);
		return ApiResponseGenerator.success(HttpStatus.OK);
	}
}
