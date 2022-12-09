package com.example.iptimeAPI.service.clubRoom;

import com.example.iptimeAPI.domain.clubRoom.ClubRoomLog;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClubRoomLogServiceImpl implements ClubRoomLogService {

    private final ClubRoomLogRepository clubRoomLogRepository;

    @Override
    public void addToDB(Long memberId) {
        ClubRoomLog clubRoomLog = new ClubRoomLog(memberId, LocalDate.now());
        clubRoomLogRepository.save(clubRoomLog);
    }

    @Override
    public List<List<Long>> calculateRanking(List<Long> memberIds) {

        Map<Long, Long> memberVistCountMap = getMemberVisitCountResult(memberIds);

        List<Map.Entry<Long, Long>> memberVisitCountList = sortReverseByValue(memberVistCountMap);

        return getCalculatedRankingResult(memberVisitCountList);
    }

    private Map<Long, Long> getMemberVisitCountResult(List<Long> memberIds) {
        Map<Long, Long> memberVistCountMap = new HashMap<>();
        for (Long memberId : memberIds) {
            long count = clubRoomLogRepository.findAllByMemberIdAndLocalDateBetween(memberId, LocalDate.now()
                            .minusMonths(1L), LocalDate.now())
                    .stream()
                    .count();
            memberVistCountMap.put(memberId, count);
        }
        return memberVistCountMap;
    }

    private static List<Map.Entry<Long, Long>> sortReverseByValue(Map<Long, Long> memberVistCountMap) {
        Set<Map.Entry<Long, Long>> memberVisitCountSet = memberVistCountMap.entrySet();
        List<Map.Entry<Long, Long>> memberVisitCountList = new LinkedList<>(memberVisitCountSet);
        memberVisitCountList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        return memberVisitCountList;
    }

    private static List<List<Long>> getCalculatedRankingResult(List<Map.Entry<Long, Long>> memberVisitCountList) {

        Map<Long, List<Long>> calculatedRankingResult = new HashMap<>();
        for (Map.Entry<Long, Long> me : memberVisitCountList) {
            if (calculatedRankingResult.containsKey(me.getValue())) {
                List<Long> memberIdList = mergeList(calculatedRankingResult.get(me.getValue()), me.getKey());
                calculatedRankingResult.put(me.getValue(), memberIdList);
            }
            if (!calculatedRankingResult.containsKey(me.getValue())) {
                calculatedRankingResult.put(me.getValue(), List.of(me.getKey()));
            }
        }

        List<Map.Entry<Long, List<Long>>> reverseByKey = sortReverseByKey(calculatedRankingResult);

        List<List<Long>> result = convertToList(reverseByKey);

        return result;
    }

    private static List<Long> mergeList(List<Long> baseList, Long value) {
        List<Long> base = baseList;
        List<Long> plus = List.of(value);

        List<Long> memberIdList = Stream.of(base, plus)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());
        return memberIdList;
    }

    private static List<Map.Entry<Long, List<Long>>> sortReverseByKey(Map<Long, List<Long>> calculatedRankingResult) {
        Set<Map.Entry<Long, List<Long>>> calculatedRankingResultSet = calculatedRankingResult.entrySet();
        List<Map.Entry<Long, List<Long>>> calculatedRankingList = new LinkedList<>(calculatedRankingResultSet);
        calculatedRankingList.sort(Map.Entry.comparingByKey(Comparator.reverseOrder()));
        return calculatedRankingList;
    }

    private static List<List<Long>> convertToList(List<Map.Entry<Long, List<Long>>> reverseByKey) {
        List<List<Long>> result = new LinkedList<>();
        for (Map.Entry<Long, List<Long>> me : reverseByKey) {
            result.add(me.getValue());
        }
        return result;
    }
}
