package com.example.iptimeAPI.web.controller.clubs;

import com.example.iptimeAPI.service.clubRoom.RankingType;
import com.example.iptimeAPI.web.dto.MacAddressDTO;
import com.example.iptimeAPI.domain.macAddress.MacAddress;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import com.example.iptimeAPI.domain.macAddress.MacAddressService;
import com.example.iptimeAPI.web.dto.MemberRankingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubsController {


    private final MacAddressService macAddressService;
    private final ClubRoomLogService clubRoomLogService;

    @GetMapping("/members")
    public List<MacAddressDTO> browseExistMember() throws IOException {
        return macAddressService.browseExistMember();
    }

    @PostMapping("/entrance")
    public void enterClub(Long memberId) throws IOException {
        MacAddress macAddress = macAddressService.validateRegisteredMember(memberId);
        macAddressService.checkMemberMacAddressIsExist(macAddress);
        clubRoomLogService.save(memberId);
    }

    @GetMapping("/rankings/{type}")
    public List<MemberRankingDTO> rankings(@PathVariable String type) {
        List<Long> memberIds = macAddressService.browseRegisteredMembers();
        return clubRoomLogService.getRanking(memberIds, RankingType.valueOf(type.toUpperCase()));
    }
}
