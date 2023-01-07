package com.example.iptimeAPI.web.controller.clubs;

import com.example.iptimeAPI.domain.iptime.IptimeService;
import com.example.iptimeAPI.domain.macAddress.MacAddress;
import com.example.iptimeAPI.service.clubRoom.RankingType;
import com.example.iptimeAPI.web.dto.IpDTO;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import com.example.iptimeAPI.domain.macAddress.MacAddressService;
import com.example.iptimeAPI.web.dto.MemberRankingDTO;
import com.example.iptimeAPI.web.exception.MacAddressValidateError;
import com.example.iptimeAPI.web.exception.MacAddressValidateException;
import com.example.iptimeAPI.web.response.ApiResponse;
import com.example.iptimeAPI.web.response.ApiResponseGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubsController {


    private final MacAddressService macAddressService;
    private final ClubRoomLogService clubRoomLogService;
    private final IptimeService iptimeService;

    @PostMapping("/in")
    public ApiResponse<ApiResponse.withData> isInClub(IpDTO ipDTO) {
        return ApiResponseGenerator.success(iptimeService.isInIptime(ipDTO), HttpStatus.OK, HttpStatus.OK.value() + "100", "in ecnv");
    }

    @GetMapping("/members")
    public ApiResponse<ApiResponse.withData> browseExistMember() {
        List<MacAddress.MacAddressResponseDTO> macAddresses = macAddressService.browseMacAddresses();
        return ApiResponseGenerator.success(iptimeService.browseExistMembers(macAddresses), HttpStatus.OK, HttpStatus.OK.value() + "100", "exist members");
    }

    @PostMapping("/entrance")
    public ApiResponse<ApiResponse.withCodeAndMessage> enterClub(Long memberId) {
        MacAddress.MacAddressResponseDTO macAddress = macAddressService.findMemberMacAddress(memberId);
        if (!iptimeService.isExistMacAddress(macAddress.getMacAddress())) {
            throw new MacAddressValidateException(MacAddressValidateError.NOT_EXIST_MACADDRESS);
        }
        clubRoomLogService.save(memberId);
        return ApiResponseGenerator.success(HttpStatus.OK, HttpStatus.OK.value() + "100", "enter ecnv");
    }

    @GetMapping("/rankings/{type}")
    public ApiResponse<ApiResponse.withData> rankings(@PathVariable String type) {
        List<Long> memberIds = macAddressService.browseMacAddressesMembers();
        List<MemberRankingDTO> ranking = clubRoomLogService.getRanking(memberIds, RankingType.valueOf(type.toUpperCase()));
        return ApiResponseGenerator.success(ranking, HttpStatus.OK, HttpStatus.OK.value() + "500", "ranking result type : " + type);
    }
}
