package com.example.iptimeAPI.service.clubRoom;

import com.example.iptimeAPI.domain.clubRoom.ClubRoomLog;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogRepository;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import com.example.iptimeAPI.web.dto.MemberRankingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClubRoomLogServiceImpl implements ClubRoomLogService {

    private final ClubRoomLogRepository repository;

    @Override
    public void save(Long memberId) {
        ClubRoomLog clubRoomLog = new ClubRoomLog(memberId, LocalDate.now());
        // todo check already exist
        // consider use redis?
        repository.save(clubRoomLog);
    }

    @Override
    public List<MemberRankingDTO> getRanking(List<Long> memberIds, RankingType type) {

        Map<Long, Long> memberVisitCount = getMemberVisitCountResult(memberIds, type);

        List<Map.Entry<Long, Long>> memberOrderByVisitCount = orderByVisitCount(memberVisitCount);

        return getCalculatedRankingResult(memberOrderByVisitCount);
    }

    // todo 기간 조정할 수 있도록
    private Map<Long, Long> getMemberVisitCountResult(List<Long> memberIds, RankingType type) {
        Map<Long, Long> memberVisitCount = new HashMap<>();
        for (Long memberId : memberIds) {
            long visitCount = repository.findAllByMemberIdAndLocalDateBetween(memberId, type.getBeforeLocalDate(), LocalDate.now())
                    .stream()
                    .count();
            memberVisitCount.put(memberId, visitCount);
        }
        return memberVisitCount;
    }

    private static List<Map.Entry<Long, Long>> orderByVisitCount(Map<Long, Long> memberVisitCount) {
        List<Map.Entry<Long, Long>> memberOrderByVisitCount = new LinkedList<>(memberVisitCount.entrySet());
        memberOrderByVisitCount.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return memberOrderByVisitCount;
    }

    private static List<MemberRankingDTO> getCalculatedRankingResult(List<Map.Entry<Long, Long>> memberOrderByVisitCount) {

        Map<Long, List<Long>> calculatedRankingResult = new HashMap<>();
        for (Map.Entry<Long, Long> member : memberOrderByVisitCount) {
            Long memberId = member.getKey();
            Long memberVisitCount = member.getValue();
            if (calculatedRankingResult.containsKey(memberVisitCount)) {
                List<Long> memberIdList = mergeList(calculatedRankingResult.get(memberVisitCount), memberId);
                calculatedRankingResult.put(memberVisitCount, memberIdList);
            }

            if (!calculatedRankingResult.containsKey(memberVisitCount)) {
                calculatedRankingResult.put(memberVisitCount, List.of(memberId));
            }
        }

        List<Map.Entry<Long, List<Long>>> rankingResultOrderByVisitCount = orderByRankingVisitCount(calculatedRankingResult);

        return convertToRanking(rankingResultOrderByVisitCount);
    }

    private static List<Long> mergeList(List<Long> baseList, Long value) {
        List<Long> base = baseList;
        List<Long> plus = List.of(value);

        List<Long> memberIdList = Stream.of(base, plus)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());
        return memberIdList;
    }

    private static List<Map.Entry<Long, List<Long>>> orderByRankingVisitCount(Map<Long, List<Long>> calculatedRankingResult) {
        List<Map.Entry<Long, List<Long>>> reverseByKey = new LinkedList<>(calculatedRankingResult.entrySet());
        reverseByKey.sort(Map.Entry.comparingByKey(Comparator.reverseOrder()));
        return reverseByKey;
    }

    private static List<MemberRankingDTO> convertToRanking(List<Map.Entry<Long, List<Long>>> rankingResultOrderByVisitCount) {
        List<List<Long>> rankingAndMemberList = getRankingAndMemberList(rankingResultOrderByVisitCount);
        return getMemberRankingDTOS(rankingAndMemberList);
    }

    private static List<List<Long>> getRankingAndMemberList(List<Map.Entry<Long, List<Long>>> rankingResultOrderByVisitCount) {
        List<List<Long>> rankingAndMemberList = new ArrayList<>();
        for (Map.Entry<Long, List<Long>> ranking : rankingResultOrderByVisitCount) {
            List<Long> members = ranking.getValue();
            Collections.shuffle(members);
            rankingAndMemberList.add(members);
        }
        return rankingAndMemberList;
    }
    private static List<MemberRankingDTO> getMemberRankingDTOS(List<List<Long>> rankingAndMemberList) {
        List<MemberRankingDTO> memberRankingDTOS = new ArrayList<>();
        for (int i = 0, j = 1; i < rankingAndMemberList.size(); i++, j++) {
            for (Long memberId : rankingAndMemberList.get(i)) {
                memberRankingDTOS.add(new MemberRankingDTO(j, memberId));
            }
        }
        return memberRankingDTOS;
    }
}
