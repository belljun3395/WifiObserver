package com.example.iptimeAPI.web.controller.clubs;

import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import com.example.iptimeAPI.domain.iptime.IptimeService;
import com.example.iptimeAPI.domain.user.UserService;
import com.example.iptimeAPI.service.facade.IptimeMacAddressFacade;
import com.example.iptimeAPI.domain.user.UserInfoVO;
import com.example.iptimeAPI.web.dto.IpInfoRequest;
import com.example.iptimeAPI.web.response.ApiResponse;
import com.example.iptimeAPI.web.response.ApiResponseGenerator;
import io.swagger.annotations.Api;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("default")
@Api(tags = {"Clubs API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
public class ClubsController {
    private final ClubRoomLogService clubRoomLogService;

    private final IptimeService iptimeService;

    private final IptimeMacAddressFacade iptimeMacAddressFacade;

    private final UserService userService;


    @GetMapping("/members")
    public ApiResponse<ApiResponse.withData> browseExistMember() {
        List<Long> members = iptimeMacAddressFacade.browseExistMembers();

        List<UserInfoVO> userInfoVOS = userService.getUsersById(members);

        return
            ApiResponseGenerator.success(
                userInfoVOS,
                HttpStatus.OK,
                HttpStatus.OK.value() + "100",
                "exist members"
            );
    }

    @PostMapping("/entrance")
    public ApiResponse<ApiResponse.withCodeAndMessage> enterClub(
                                                                    IpInfoRequest ipInfoRequest,
                                                                    @RequestHeader(value = "Authorization") String accessToken
                                                                ) throws IOException {
        if (!iptimeService.isInIptime(ipInfoRequest)) {
            return
                ApiResponseGenerator.success(
                    HttpStatus.OK,
                    HttpStatus.OK.value() + "100",
                    "enter ecnv"
                );
        }

        Long memberId = userService.getUserByToken(accessToken)
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