package com.example.iptimeAPI.service.clubRoom;

import com.example.iptimeAPI.domain.clubRoom.ClubRoomLog;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogRepository;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
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
    public Map<Long, List<Long>> calcRankings(List<Long> memberIds, LogPeriod type) {

        Map<Long, Long> memberVisitCount = getMemberVisitCountResult(memberIds, type);

        return calculateMemberVisitCount(memberVisitCount);
    }

    private Map<Long, Long> getMemberVisitCountResult(List<Long> memberIds, LogPeriod type) {
        Map<Long, Long> memberVisitCount = new HashMap<>();
        for (Long id : memberIds) {
            long visitCount = repository.findAllByMemberIdAndLocalDateBetween(id, type.getBeforeLocalDate(), LocalDate.now())
                    .stream()
                    .count();
            memberVisitCount.put(id, visitCount);
        }
        return memberVisitCount;
    }


    private Map<Long, List<Long>> calculateMemberVisitCount(Map<Long, Long> memberOrderByVisitCount) {
        return calculateMemberVisitCountResult(memberOrderByVisitCount, Comparator.reverseOrder());
    }

    private Map<Long, List<Long>> calculateMemberVisitCountResult(Map<Long, Long> memberOrderByVisitCount, Comparator comparator) {
        Map<Long, List<Long>> calculatedRankingResult = new TreeMap<>(comparator);
        memberOrderByVisitCount.forEach((memberId, visitCount) -> {
            if (calculatedRankingResult.containsKey(visitCount)) {
                calculatedRankingResult.merge(
                        visitCount, List.of(memberId),
                        (base, plus) ->
                                Stream.of(base, plus)
                                        .flatMap(Collection::stream)
                                        .collect(Collectors.toList())
                );
            }

            if (!calculatedRankingResult.containsKey(visitCount)) {
                calculatedRankingResult.put(visitCount, List.of(memberId));
            }
                }
        );
        return calculatedRankingResult;
    }

    @Override
    public  Long calcRanking(Map<Long, List<Long>> rankings, Long memberId) {
        return rankings.entrySet()
                .stream()
                .filter(r -> r.getValue()
                        .contains(memberId))
                .findFirst()
                .get()
                .getKey();
    }

    @Override
    public Long calcVisitCount(Long memberId, LogPeriod type) {
        return (long) repository.findAllByMemberId(memberId).size();
    }
}
