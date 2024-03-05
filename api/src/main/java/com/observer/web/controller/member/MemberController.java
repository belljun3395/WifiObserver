package com.observer.web.controller.member;

import com.observer.domain.dto.member.CheckCertificationDuplicationUseCaseRequest;
import com.observer.domain.dto.member.CheckCertificationDuplicationUseCaseResponse;
import com.observer.domain.dto.member.DeleteMemberUseCaseRequest;
import com.observer.domain.dto.member.LoginMemberUseCaseRequest;
import com.observer.domain.dto.member.LoginMemberUseCaseResponse;
import com.observer.domain.dto.member.PostMemberUseCaseRequest;
import com.observer.domain.dto.member.RenewalApiKeyUseCaseRequest;
import com.observer.domain.dto.member.RenewalApiKeyUseCaseResponse;
import com.observer.domain.usecase.member.CheckCertificationDuplicationUseCase;
import com.observer.domain.usecase.member.DeleteMemberUseCase;
import com.observer.domain.usecase.member.LoginMemberUseCase;
import com.observer.domain.usecase.member.PostMemberUseCase;
import com.observer.domain.usecase.member.RenewalApiKeyUseCase;
import com.observer.security.authentication.api.ApiKeyUserDetails;
import com.observer.web.controller.dto.member.LoginRequest;
import com.observer.web.controller.dto.member.MemberDeleteRequest;
import com.observer.web.controller.dto.member.MemberSaveRequest;
import com.observer.web.controller.dto.member.RenewalApiKeyRequest;
import com.observer.web.controller.dto.validator.Certification;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

	private final PostMemberUseCase postMemberUseCase;
	private final LoginMemberUseCase loginMemberUseCase;
	private final RenewalApiKeyUseCase renewalApiKeyUseCase;
	private final CheckCertificationDuplicationUseCase checkCertificationDuplicationUseCase;
	private final DeleteMemberUseCase deleteMemberUseCase;

	@PostMapping()
	public ApiResponse<ApiResponse.Success> save(@Valid @RequestBody MemberSaveRequest request) {
		PostMemberUseCaseRequest useCaseRequest =
				PostMemberUseCaseRequest.builder()
						.certification(request.getCertification())
						.password(request.getPassword())
						.build();
		postMemberUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(HttpStatus.CREATED, MessageCode.RESOURCE_CREATED);
	}

	@PostMapping("/login")
	public ApiResponse<ApiResponse.SuccessBody<LoginMemberUseCaseResponse>> login(
			@Valid @RequestBody LoginRequest request) {
		LoginMemberUseCaseRequest useCaseRequest =
				LoginMemberUseCaseRequest.builder()
						.certification(request.getCertification())
						.password(request.getPassword())
						.build();
		LoginMemberUseCaseResponse response = loginMemberUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PostMapping("/delete")
	public ApiResponse<ApiResponse.Success> delete(
			@AuthenticationPrincipal ApiKeyUserDetails userDetails,
			@Valid @RequestBody MemberDeleteRequest request) {
		DeleteMemberUseCaseRequest useCaseRequest =
				DeleteMemberUseCaseRequest.builder()
						.apiKey(userDetails.getApiKeyValue())
						.password(request.getPassword())
						.build();
		deleteMemberUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_DELETED);
	}

	@PutMapping("/key")
	public ApiResponse<ApiResponse.SuccessBody<RenewalApiKeyUseCaseResponse>> renewalApiKey(
			@RequestBody RenewalApiKeyRequest request) {
		RenewalApiKeyUseCaseRequest useCaseRequest =
				RenewalApiKeyUseCaseRequest.builder()
						.certification(request.getCertification())
						.password(request.getPassword())
						.build();
		RenewalApiKeyUseCaseResponse response = renewalApiKeyUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.RESOURCE_MODIFIED);
	}

	@GetMapping("/check/certification/{certification}")
	public ApiResponse<ApiResponse.SuccessBody<CheckCertificationDuplicationUseCaseResponse>> check(
			@Certification @PathVariable(value = "certification") String certification) {
		CheckCertificationDuplicationUseCaseRequest useCaseRequest =
				CheckCertificationDuplicationUseCaseRequest.builder().certification(certification).build();
		CheckCertificationDuplicationUseCaseResponse response =
				checkCertificationDuplicationUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}
}
