package com.example.iptimeAPI.web.controller.clubs;

import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import com.example.iptimeAPI.domain.clubRoom.RankingsVO;
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
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
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
        RankingsVO rankingsVO = clubRoomLogService.browseRanking(
            LogPeriod.valueOf(period.toUpperCase())).get();

        // todo mapper 고민
        List<MemberRankingDTO> memberRankingDTOS = shuffleAndMapDTO(
            rankingsVO);

        return ApiResponseGenerator.success(memberRankingDTOS, HttpStatus.OK,
            HttpStatus.OK.value() + "500", "ranking result period : " + period);
    }

    private List<MemberRankingDTO> shuffleAndMapDTO(RankingsVO rankingsVO) {
        Set<Entry<Long, List<Long>>> rankingEntries = rankingsVO.getRankings().entrySet();
        List<MemberRankingDTO> memberRankingDTOS = new ArrayList<>();

        for (Entry<Long, List<Long>> rankingEntry : rankingEntries) {
            List<Long> memberIds = rankingEntry.getValue();
            Collections.shuffle(memberIds);
            for (Long memberId : memberIds) {
                memberRankingDTOS.add(
                    new MemberRankingDTO(rankingEntry.getKey(), userService.getUserById(memberId)));
            }
        }
        return memberRankingDTOS;
    }


    @GetMapping("/member")
    public ApiResponse<ApiResponse.withData> memberRankingCountInfo(
        @RequestHeader(value = "Authorization") String accessToken,
        @ApiParam(example = "month") @RequestParam String period) {

        LogPeriod periodType = LogPeriod.valueOf(period.toUpperCase());
        RankingsVO rankingsVO = clubRoomLogService.browseRanking(
            LogPeriod.valueOf(period.toUpperCase())).get();

        UserInfoVO user = userService.getUserByToken(accessToken);
        Long memberRanking = clubRoomLogService.calcMemberRanking(rankingsVO.getRankings(),
            user.getId());
        Long visitCount = clubRoomLogService.browseMemberVisitCount(user.getId(), periodType);

        MemberRankingInfoDTO memberRankingInfoDTO = new MemberRankingInfoDTO(user.getYear(),
            user.getName(), user.getId(), memberRanking, visitCount);
        return ApiResponseGenerator.success(memberRankingInfoDTO, HttpStatus.OK,
            HttpStatus.OK.value() + "500", "ranking result period : " + period);
    }
}
