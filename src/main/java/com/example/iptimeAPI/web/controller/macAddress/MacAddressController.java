package com.example.iptimeAPI.web.controller.macAddress;

import com.example.iptimeAPI.domain.macAddress.MacAddressService;
import com.example.iptimeAPI.service.user.UserServiceImpl;
import com.example.iptimeAPI.web.dto.MacAddressEditDTO;
import com.example.iptimeAPI.web.dto.MacAddressRegistDTO;
import com.example.iptimeAPI.web.response.ApiResponse;
import com.example.iptimeAPI.web.response.ApiResponseGenerator;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"MacAddress API"})
@RestController
@RequestMapping("/api/macs")
@RequiredArgsConstructor
public class MacAddressController {

    private final MacAddressService macAddressService;
    private final UserServiceImpl userServiceImpl;

    @PostMapping
    public ApiResponse<ApiResponse.withCodeAndMessage> registerMacAddress(
        MacAddressRegistDTO macAddressRegistDTO) {
        macAddressService.registerMacAddress(macAddressRegistDTO);
        return ApiResponseGenerator.success(HttpStatus.OK, HttpStatus.OK.value() + "600",
            "success register");
    }

    @GetMapping
    public ApiResponse<ApiResponse.withData> findMemberMacAddress(
        @RequestHeader(value = "Authorization") String accessToken) {
        Long memberId = userServiceImpl.getUserByToken(accessToken)
            .getId();
        return ApiResponseGenerator.success(macAddressService.findMemberMacAddress(memberId),
            HttpStatus.OK, HttpStatus.OK.value() + "600", "member's mac address info");
    }

    @PutMapping
    public ApiResponse<ApiResponse.withCodeAndMessage> editMemberMacAddress(
        MacAddressEditDTO macAddressEditDTO) {
        macAddressService.editMacAddress(macAddressEditDTO);
        return ApiResponseGenerator.success(HttpStatus.OK, HttpStatus.OK.value() + "600",
            "success edit mac address info");
    }

}
