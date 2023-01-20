package com.example.iptimeAPI.service.clubRoom;

import com.example.iptimeAPI.domain.clubRoom.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
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
    private final RankingsRepository rankingsRepository;

    @Override
    @Transactional
    public boolean save(Long memberId) {
        Optional<ClubRoomLog> byMemberId = repository.findByMemberIdAndLocalDate(memberId, LocalDate.now());
        if (byMemberId.isPresent()) {
            return false;
        }
        repository.save(new ClubRoomLog(memberId, LocalDate.now()));
        return true;
    }

    @Async
    @EventListener
    @Transactional
    public void saveRankings(EnterClubEvent enterClubEvent) {
        List<Long> memberIds = enterClubEvent.getMemberIds();

        RankingsVO rankingVOYear = getRanking(LogPeriod.YEAR);
        RankingsVO rankingVOMonth = getRanking(LogPeriod.MONTH);
        RankingsVO rankingVOWeek = getRanking(LogPeriod.WEEK);

        Map<Long, List<Long>> rankingYear = calcRanking(memberIds, LogPeriod.YEAR);
        Map<Long, List<Long>> rankingMonth = calcRanking(memberIds, LogPeriod.MONTH);
        Map<Long, List<Long>> rankingWeek = calcRanking(memberIds, LogPeriod.WEEK);

        saveRanking(rankingVOYear.compareRanking(rankingYear), LogPeriod.YEAR);
        saveRanking(rankingVOMonth.compareRanking(rankingMonth), LogPeriod.MONTH);
        saveRanking(rankingVOWeek.compareRanking(rankingWeek), LogPeriod.WEEK);
    }

    private void saveRanking(Map<Long, List<Long>> ranking, LogPeriod period) {
        rankingsRepository.save(new RankingsVO(ranking, period));
    }

    private Map<Long, List<Long>> calcRanking(List<Long> memberIds, LogPeriod period) {
        Map<Long, Long> memberVisitCount = getMemberVisitCountResult(memberIds, period);
        Map<Long, List<Long>> rankings = calculateMemberVisitCount(memberVisitCount);
        return rankings;
    }

    public RankingsVO getRanking(LogPeriod period) {
        return rankingsRepository.findRecent(period)
                .stream()
                .max(Comparator.comparing(RankingsVO::getLocalDateTime))
                .orElseThrow(() -> {
                    throw new IllegalStateException("no ranking result");
                });
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
    public Long calcRanking(Map<Long, List<Long>> rankings, Long memberId) {
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
        return (long) repository.findAllByMemberId(memberId)
                .size();
    }
}
