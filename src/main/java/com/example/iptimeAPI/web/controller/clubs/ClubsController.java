package com.example.iptimeAPI.web.controller.clubs;

import com.example.iptimeAPI.domain.iptime.IptimeService;
import com.example.iptimeAPI.service.clubRoom.RankingType;
import com.example.iptimeAPI.web.dto.IpDTO;
import com.example.iptimeAPI.domain.macAddress.MacAddress;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import com.example.iptimeAPI.domain.macAddress.MacAddressService;
import com.example.iptimeAPI.web.dto.MemberRankingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubsController {


    private final MacAddressService macAddressService;
    private final ClubRoomLogService clubRoomLogService;
    private final IptimeService iptimeService;

    @GetMapping("/in")
    public boolean isInClub(IpDTO ipDTO) {
        return iptimeService.isInIptime(ipDTO);
    }

    @GetMapping("/members")
    public List<Long> browseExistMember() {
        List<MacAddress> macAddresses = macAddressService.browseMacAddresses();
        return iptimeService.browseExistMembers(macAddresses);
    }

    @PostMapping("/entrance")
    public void enterClub(Long memberId) {
        MacAddress macAddress = macAddressService.findMemberMacAddress(memberId);
        if (!iptimeService.isExistMacAddress(macAddress)) {
            throw new IllegalStateException("need macAddress regist");
        }
        clubRoomLogService.save(memberId);
    }

    @GetMapping("/rankings/{type}")
    public List<MemberRankingDTO> rankings(@PathVariable String type) {
        List<Long> memberIds = macAddressService.browseMacAddressesMembers();
        return clubRoomLogService.getRanking(memberIds, RankingType.valueOf(type.toUpperCase()));
    }
}
