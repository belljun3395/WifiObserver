package com.example.iptimeAPI.service.clubRoom;

import com.example.iptimeAPI.domain.clubRoom.ClubRoomLog;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogRepository;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import com.example.iptimeAPI.web.dto.MemberRankingDTO;
import com.example.iptimeAPI.web.fegin.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubRoomLogServiceImpl implements ClubRoomLogService {

    private final ClubRoomLogRepository repository;

    @Override
    @Transactional
    public void save(Long memberId) {
        Optional<ClubRoomLog> byMemberId = repository.findByMemberIdAndLocalDate(memberId, LocalDate.now());
        if (byMemberId.isPresent()) {
            return;
        }
        repository.save(new ClubRoomLog(memberId, LocalDate.now()));
    }

    @Override
    public List<MemberRankingDTO> getRanking(List<UserInfo> userInfos, RankingType type) {

        Map<UserInfo, Long> memberVisitCount = getMemberVisitCountResult(userInfos, type);

        Map<Long, List<UserInfo>> memberVisitCountResult = calculateMemberVisitCount(memberVisitCount);

        List<List<UserInfo>> rankingResults = convertToRanking(memberVisitCountResult);

        return convertToDTOs(rankingResults);
    }

    private Map<UserInfo, Long> getMemberVisitCountResult(List<UserInfo> userInfos, RankingType type) {
        Map<UserInfo, Long> memberVisitCount = new HashMap<>();
        for (UserInfo userInfo : userInfos) {
            long visitCount = repository.findAllByMemberIdAndLocalDateBetween(userInfo.getId(), type.getBeforeLocalDate(), LocalDate.now())
                    .stream()
                    .count();
            memberVisitCount.put(userInfo, visitCount);
        }
        return memberVisitCount;
    }


    private Map<Long, List<UserInfo>> calculateMemberVisitCount(Map<UserInfo, Long> memberOrderByVisitCount) {
        Map<Long, List<UserInfo>> calculatedMemberVisitCountResult = calculateMemberVisitCountResult(memberOrderByVisitCount);

        return orderByCount(calculatedMemberVisitCountResult);
    }

    private Map<Long, List<UserInfo>> calculateMemberVisitCountResult(Map<UserInfo, Long> memberOrderByVisitCount) {
        Map<Long, List<UserInfo>> calculatedRankingResult = new HashMap<>();
        memberOrderByVisitCount.forEach((userInfoDTO, visitCount) -> {
                if (calculatedRankingResult.containsKey(visitCount)) {
                    calculatedRankingResult.merge(
                            visitCount, List.of(userInfoDTO),
                            (base, plus) ->
                                    Stream.of(base, plus)
                                            .flatMap(Collection::stream)
                                            .collect(Collectors.toList())
                    );
                }

                if (!calculatedRankingResult.containsKey(visitCount)) {
                    calculatedRankingResult.put(visitCount, List.of(userInfoDTO));
                }
            }
        );
        return calculatedRankingResult;
    }

    private Map<Long, List<UserInfo>> orderByCount(Map<Long, List<UserInfo>> calculatedRankingResult) {
        Map<Long, List<UserInfo>> reverseByKey = new TreeMap<>(Comparator.reverseOrder());
        reverseByKey.putAll(calculatedRankingResult);
        return reverseByKey;
    }

    private List<List<UserInfo>> convertToRanking(Map<Long, List<UserInfo>> rankingResultOrderByVisitCount) {
        return rankingResultOrderByVisitCount.values()
                .stream()
                .peek(Collections::shuffle)
                .collect(Collectors.toList());
    }

    private List<MemberRankingDTO> convertToDTOs(List<List<UserInfo>> rankingAndMemberList) {
        List<MemberRankingDTO> memberRankingDTOS = new ArrayList<>();
        for (int i = 0, j = 1; i < rankingAndMemberList.size(); i++, j++) {
            for (UserInfo userInfo : rankingAndMemberList.get(i)) {
                memberRankingDTOS.add(new MemberRankingDTO(j, userInfo));
            }
        }
        return memberRankingDTOS;
    }
}
