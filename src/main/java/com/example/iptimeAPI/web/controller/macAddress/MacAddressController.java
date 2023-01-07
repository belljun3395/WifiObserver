package com.example.iptimeAPI.web.controller.macAddress;

import com.example.iptimeAPI.domain.macAddress.MacAddress;
import com.example.iptimeAPI.web.dto.MacAddressEditDTO;
import com.example.iptimeAPI.web.dto.MacAddressRegistDTO;
import com.example.iptimeAPI.domain.macAddress.MacAddressService;
import com.example.iptimeAPI.web.response.ApiResponse;
import com.example.iptimeAPI.web.response.ApiResponseGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"MacAddress API"})
@RestController
@RequestMapping("/api/macs")
@RequiredArgsConstructor
public class MacAddressController {

    private final MacAddressService macAddressService;

    @PostMapping
    public ApiResponse<ApiResponse.withCodeAndMessage> registerMacAddress(MacAddressRegistDTO macAddressRegistDTO) {
        macAddressService.registerMacAddress(macAddressRegistDTO);
        return ApiResponseGenerator.success(HttpStatus.OK, HttpStatus.OK.value() + "600", "success register");
    }

    @GetMapping("/{memberId}")
    public ApiResponse<ApiResponse.withData> findMemberMacAddress(@ApiParam(example = "1") @PathVariable("memberId") Long memberId) {
        MacAddress.MacAddressResponseDTO memberMacAddress = macAddressService.findMemberMacAddress(memberId);
        MacAddress.MacAddressResponseDTO macAddressResponseDTO = new MacAddress.MacAddressResponseDTO(memberMacAddress.getId(), memberMacAddress.getMemberId(), memberMacAddress.getMacAddress());
        return ApiResponseGenerator.success(macAddressResponseDTO, HttpStatus.OK, HttpStatus.OK.value() + "600", "member's mac address info");
    }

    @PutMapping
    public ApiResponse<ApiResponse.withCodeAndMessage> editMemberMacAddress(MacAddressEditDTO macAddressEditDTO) {
        macAddressService.editMacAddress(macAddressEditDTO);
        return ApiResponseGenerator.success(HttpStatus.OK, HttpStatus.OK.value() + "600", "success edit mac address info");
    }

}
