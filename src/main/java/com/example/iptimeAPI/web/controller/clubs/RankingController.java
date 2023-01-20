package com.example.iptimeAPI.web.controller.clubs;

import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import com.example.iptimeAPI.domain.clubRoom.RankingsVO;
import com.example.iptimeAPI.service.clubRoom.LogPeriod;
import com.example.iptimeAPI.service.user.UserServiceImpl;
import com.example.iptimeAPI.service.user.dto.UserInfoVO;
import com.example.iptimeAPI.web.dto.MemberRankingDTO;
import com.example.iptimeAPI.web.dto.MemberRankingInfoDTO;
import com.example.iptimeAPI.web.response.ApiResponse;
import com.example.iptimeAPI.web.response.ApiResponseGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = {"Ranking API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rankings")
public class RankingController {
    private final ClubRoomLogService clubRoomLogService;
    private final UserServiceImpl userServiceImpl;

    @GetMapping
    public ApiResponse<ApiResponse.withData> rankings(@ApiParam(example = "month") @RequestParam String period) {
        RankingsVO rankingsVO = clubRoomLogService.getRanking(LogPeriod.valueOf(period.toUpperCase()));
        List<List<Long>> rankingMemberIds = new ArrayList<>(shuffleRankings(rankingsVO.getRankings()));

        List<MemberRankingDTO> memberRankingDTOS = new ArrayList<>();
        for (int i = 0, j = 1; i < rankingMemberIds.size(); i++, j++) {
            for (Long memberId : rankingMemberIds.get(i)) {
                memberRankingDTOS.add(new MemberRankingDTO(j, userServiceImpl.getUserById(memberId)));
            }
        }

        return ApiResponseGenerator.success(memberRankingDTOS, HttpStatus.OK, HttpStatus.OK.value() + "500", "ranking result period : " + period);
    }

    private List<List<Long>> shuffleRankings(Map<Long, List<Long>> rankings) {
        return rankings.values()
                .stream()
                .peek(Collections::shuffle)
                .collect(Collectors.toList());
    }


    @GetMapping("/member")
    public ApiResponse<ApiResponse.withData> memberRankingCountInfo(@RequestHeader(value = "Authorization") String accessToken, @RequestParam String period) {
        LogPeriod periodType = LogPeriod.valueOf(period.toUpperCase());
        RankingsVO rankingsVO = clubRoomLogService.getRanking(LogPeriod.valueOf(period.toUpperCase()));

        UserInfoVO user = userServiceImpl.getUserByToken(accessToken);
        Long memberRanking = clubRoomLogService.calcRanking(rankingsVO.getRankings(), user.getId());
        Long visitCount = clubRoomLogService.calcVisitCount(user.getId(), periodType);

        MemberRankingInfoDTO memberRankingInfoDTO = new MemberRankingInfoDTO(user.getYear(), user.getName(), user.getId(), memberRanking, visitCount);
        return ApiResponseGenerator.success(memberRankingInfoDTO, HttpStatus.OK, HttpStatus.OK.value() + "500", "ranking result period : " + period);
    }
}
