package com.example.iptimeAPI.web.dev.controller.clubs;

import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import com.example.iptimeAPI.domain.iptime.IptimeService;
import com.example.iptimeAPI.domain.user.UserService;
import com.example.iptimeAPI.service.facade.IptimeMacAddressFacade;
import com.example.iptimeAPI.service.user.UserServiceImpl;
import com.example.iptimeAPI.service.user.dto.UserInfoVO;
import com.example.iptimeAPI.web.dto.IpDTO;
import com.example.iptimeAPI.web.response.ApiResponse;
import com.example.iptimeAPI.web.response.ApiResponseGenerator;
import io.swagger.annotations.Api;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@Api(tags = {"Clubs API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
public class ClubsController {

    private final ClubRoomLogService clubRoomLogService;
    private final IptimeService iptimeService;
    private final IptimeMacAddressFacade iptimeMacAddressFacade;
    private final UserServiceImpl userServiceImpl;


    @GetMapping("/members")
    public ApiResponse<ApiResponse.withData> browseExistMember() {
        List<Long> members = iptimeMacAddressFacade.browseExistMembers();

        List<UserInfoVO> userInfoVOS = new ArrayList<>();
        for (Long memberId : members) {
            userInfoVOS.add(
                userServiceImpl.getUserById_dev(memberId)
            );
        }

        return
            ApiResponseGenerator.success(
                userInfoVOS,
                HttpStatus.OK,
                HttpStatus.OK.value() + "100",
                "exist members");
    }

    @PostMapping("/entrance")
    public ApiResponse<ApiResponse.withCodeAndMessage> enterClub(
        IpDTO ipDTO,
        @RequestParam Long id
    )
        throws IOException {

        if (!iptimeService
            .isInIptime(ipDTO)
            .isIn()
        ) {
            return
                ApiResponseGenerator.success(
                    HttpStatus.OK,
                    HttpStatus.OK.value() + "100",
                    "enter ecnv"
                );
        }

        Long memberId =
            userServiceImpl
                .getUserById_dev(id)
                .getId();

        iptimeMacAddressFacade.validateExistMemberMacAddress(memberId);

        clubRoomLogService.save(memberId);

        return
            ApiResponseGenerator.success(
                HttpStatus.OK,
                HttpStatus.OK.value() + "100",
                "enter ecnv"
            );
    }
}