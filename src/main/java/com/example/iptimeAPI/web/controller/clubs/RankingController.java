package com.example.iptimeAPI.web.controller.clubs;

import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import com.example.iptimeAPI.domain.user.UserService;
import com.example.iptimeAPI.service.clubRoom.LogPeriod;
import com.example.iptimeAPI.domain.user.UserInfoVO;
import com.example.iptimeAPI.web.dto.MemberRankingResponse;
import com.example.iptimeAPI.web.dto.MemberRankingCountResponse;
import com.example.iptimeAPI.web.response.ApiResponse;
import com.example.iptimeAPI.web.response.ApiResponseGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("default")
@Api(tags = {"Ranking API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rankings")
public class RankingController {
    private final ClubRoomLogService clubRoomLogService;

    private final UserService userService;


    @GetMapping
    public ApiResponse<ApiResponse.withData> rankings(@ApiParam(example = "month") @RequestParam String period) {

        Map<Long, Long> membersRanking =
            clubRoomLogService
                .calcRanking(
                    LogPeriod.valueOf(period.toUpperCase())
                );

        List<MemberRankingResponse> memberRankingDTOS = shuffleAndMapDTO(membersRanking);

        return
            ApiResponseGenerator.success(
                memberRankingDTOS,
                HttpStatus.OK,
                HttpStatus.OK.value() + "500",
                "ranking result period : " + period);
    }

    private List<MemberRankingResponse> shuffleAndMapDTO(Map<Long, Long> membersRanking) {
        List<Long> memberIds = new ArrayList<>(membersRanking.keySet());

        List<UserInfoVO> userInfoVOS = userService.getUsersById(memberIds);

        List<MemberRankingResponse> memberRankingDTOS = new ArrayList<>();

        for (int i = 0; i < memberIds.size(); i++) {
            memberRankingDTOS.add(new MemberRankingResponse(membersRanking.get(i), userInfoVOS.get(i)));
        }

        return memberRankingDTOS;
    }

    @GetMapping("/member")
    public ApiResponse<ApiResponse.withData> memberRankingCountInfo(
                                                                    @RequestHeader(value = "Authorization") String accessToken,
                                                                    @ApiParam(example = "month") @RequestParam String period
                                                                    ) {

        LogPeriod periodType = LogPeriod.valueOf(period.toUpperCase());

        UserInfoVO user = userService.getUserByToken(accessToken);

        Map<Long, Long> membersRanking = clubRoomLogService.calcRanking(LogPeriod.valueOf(period.toUpperCase()));

        Long memberRanking = membersRanking.get(user.getId());

        Long visitCount = clubRoomLogService.browseMemberVisitCount(user.getId(), periodType);

        MemberRankingCountResponse memberRankingInfoDTO =
            new MemberRankingCountResponse(
                user,
                memberRanking,
                visitCount
            );

        return
            ApiResponseGenerator.success(
                memberRankingInfoDTO,
                HttpStatus.OK,
                HttpStatus.OK.value() + "500",
                "ranking result period : " + period
            );
    }
}
