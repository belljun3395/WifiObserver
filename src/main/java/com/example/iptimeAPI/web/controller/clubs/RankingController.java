package com.example.iptimeAPI.web.controller.clubs;

import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import com.example.iptimeAPI.domain.user.UserService;
import com.example.iptimeAPI.service.clubRoom.LogPeriod;
import com.example.iptimeAPI.service.user.dto.UserInfoVO;
import com.example.iptimeAPI.web.dto.MemberRankingDTO;
import com.example.iptimeAPI.web.dto.MemberRankingInfoDTO;
import com.example.iptimeAPI.web.response.ApiResponse;
import com.example.iptimeAPI.web.response.ApiResponseGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Ranking API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rankings")
public class RankingController {

    private final ClubRoomLogService clubRoomLogService;
    private final UserService userService;

    @GetMapping
    public ApiResponse<ApiResponse.withData> rankings(
        @ApiParam(example = "month") @RequestParam String period) {
        Map<Long, Long> membersRanking = clubRoomLogService.calcRanking(
            LogPeriod.valueOf(period.toUpperCase()));

        List<MemberRankingDTO> memberRankingDTOS = shuffleAndMapDTO(
            membersRanking);

        return ApiResponseGenerator.success(memberRankingDTOS, HttpStatus.OK,
            HttpStatus.OK.value() + "500", "ranking result period : " + period);
    }

    private List<MemberRankingDTO> shuffleAndMapDTO(Map<Long, Long> membersRanking) {
        List<Long> memberIds = new ArrayList<>(membersRanking.keySet());

        List<MemberRankingDTO> memberRankingDTOS = new ArrayList<>();
        for (Long memberId : memberIds) {
            memberRankingDTOS.add(
                new MemberRankingDTO(membersRanking.get(memberId),
                    userService.getUserById(memberId)));
        }

        return memberRankingDTOS;
    }


    @GetMapping("/member")
    public ApiResponse<ApiResponse.withData> memberRankingCountInfo(
        @RequestHeader(value = "Authorization") String accessToken,
        @ApiParam(example = "month") @RequestParam String period) {

        LogPeriod periodType = LogPeriod.valueOf(period.toUpperCase());
        Map<Long, Long> membersRanking = clubRoomLogService.calcRanking(
            LogPeriod.valueOf(period.toUpperCase()));

        UserInfoVO user = userService.getUserByToken(accessToken);
        Long memberRanking = membersRanking.get(user.getId());
        Long visitCount = clubRoomLogService.browseMemberVisitCount(user.getId(), periodType);

        MemberRankingInfoDTO memberRankingInfoDTO = new MemberRankingInfoDTO(user.getYear(),
            user.getName(), user.getId(), memberRanking, visitCount);

        return ApiResponseGenerator.success(memberRankingInfoDTO, HttpStatus.OK,
            HttpStatus.OK.value() + "500", "ranking result period : " + period);
    }
}
