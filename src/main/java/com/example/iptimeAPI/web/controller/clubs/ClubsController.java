package com.example.iptimeAPI.web.controller.clubs;

import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import com.example.iptimeAPI.domain.iptime.IptimeService;
import com.example.iptimeAPI.domain.macAddress.MacAddress;
import com.example.iptimeAPI.domain.macAddress.MacAddressService;
import com.example.iptimeAPI.service.clubRoom.EnterClubEvent;
import com.example.iptimeAPI.service.macAddress.exception.MacAddressValidateException;
import com.example.iptimeAPI.service.user.UserServiceImpl;
import com.example.iptimeAPI.service.user.dto.UserInfoDTO;
import com.example.iptimeAPI.web.dto.IpDTO;
import com.example.iptimeAPI.web.response.ApiResponse;
import com.example.iptimeAPI.web.response.ApiResponseGenerator;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Api(tags = {"Clubs API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
public class ClubsController {


    private final MacAddressService macAddressService;
    private final ClubRoomLogService clubRoomLogService;
    private final IptimeService iptimeService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserServiceImpl userServiceImpl;


    @GetMapping("/members")
    public ApiResponse<ApiResponse.withData> browseExistMember() {
        List<MacAddress.MacAddressResponseDTO> macAddresses = macAddressService.browseMacAddresses();
        List<Long> members = iptimeService.browseExistMembers(macAddresses);
        List<UserInfoDTO> userInfoDTOS = new ArrayList<>();
        for (Long memberId : members) {
            userInfoDTOS.add(userServiceImpl.getUserById(memberId));
        }
        return ApiResponseGenerator.success(userInfoDTOS, HttpStatus.OK, HttpStatus.OK.value() + "100", "exist members");
    }

    @PostMapping("/entrance")
    public ApiResponse<ApiResponse.withCodeAndMessage> enterClub(IpDTO ipDTO, @RequestHeader(value = "Authorization") String accessToken) throws IOException {
        if (!iptimeService.isInIptime(ipDTO)
                .isIn()) {
            return ApiResponseGenerator.success(HttpStatus.OK, HttpStatus.OK.value() + "100", "enter ecnv");
        }
        Long memberId = userServiceImpl.getUserByToken(accessToken)
                .getId();
        String macAddress = macAddressService.findMemberMacAddress(memberId)
                .getMacAddress();
        try {
            iptimeService.isExistMacAddress(macAddress);
        } catch (MacAddressValidateException macAddressValidateException) {
            iptimeService.renewalList();
            iptimeService.isExistMacAddress(macAddress);
        }

        if (clubRoomLogService.save(memberId)) {
            List<Long> memberIds = macAddressService.browseMacAddressesMembers();
            applicationEventPublisher.publishEvent(new EnterClubEvent(memberIds));
        }

        return ApiResponseGenerator.success(HttpStatus.OK, HttpStatus.OK.value() + "100", "enter ecnv");
    }
}
