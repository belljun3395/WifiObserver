package com.example.iptimeAPI.web.controller.macAddress;

import com.example.iptimeAPI.domain.macAddress.MacAddressService;
import com.example.iptimeAPI.domain.user.UserService;
import com.example.iptimeAPI.mapper.macAddress.MacAddressDTO;
import com.example.iptimeAPI.web.dto.MacAddressEditRequest;
import com.example.iptimeAPI.web.dto.MacAddressRegistRequest;
import com.example.iptimeAPI.web.dto.MacAddressResponse;
import com.example.iptimeAPI.web.response.ApiResponse;
import com.example.iptimeAPI.web.response.ApiResponseGenerator;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("default")
@Api(tags = {"MacAddress API"})
@RestController
@RequestMapping("/api/macs")
@RequiredArgsConstructor
public class MacAddressController {
    private final MacAddressService macAddressService;

    private final UserService userServiceImpl;


    @PostMapping
    public ApiResponse<ApiResponse.withCodeAndMessage> registerMacAddress(MacAddressRegistRequest macAddressRegistRequest) {

        macAddressService.registerMacAddress(macAddressRegistRequest);

        return
            ApiResponseGenerator.success(
                HttpStatus.OK,
                HttpStatus.OK.value() + "600",
                "success register"
            );
    }

    @GetMapping
    public ApiResponse<ApiResponse.withData> findMemberMacAddress(@RequestHeader(value = "Authorization") String accessToken) {
        Long memberId =
            userServiceImpl
                .getUserByToken(accessToken)
                .getId();

        MacAddressDTO memberMacAddress = macAddressService.findMemberMacAddress(memberId);

        MacAddressResponse macAddressResponse = new MacAddressResponse(memberMacAddress);

        return
            ApiResponseGenerator.success(
                macAddressResponse,
                HttpStatus.OK,
                HttpStatus.OK.value() + "600",
                "member's mac address info"
            );
    }

    @PutMapping
    public ApiResponse<ApiResponse.withCodeAndMessage> editMemberMacAddress(MacAddressEditRequest macAddressEditRequest) {
        macAddressService.editMacAddress(macAddressEditRequest);

        return
            ApiResponseGenerator.success(
                HttpStatus.OK,
                HttpStatus.OK.value() + "600",
                "success edit mac address info"
            );
    }
}