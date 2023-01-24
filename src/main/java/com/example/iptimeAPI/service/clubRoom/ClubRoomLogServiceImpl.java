package com.example.iptimeAPI.service.clubRoom;

import com.example.iptimeAPI.domain.clubRoom.ClubRoomLog;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogRepository;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import com.example.iptimeAPI.domain.clubRoom.RankingsRepository;
import com.example.iptimeAPI.domain.clubRoom.RankingsVO;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubRoomLogServiceImpl implements ClubRoomLogService {

    private final ClubRoomLogRepository repository;
    private final RankingsRepository rankingsRepository;

    @Override
    @Transactional
    public boolean save(Long memberId) {
        Optional<ClubRoomLog> byMemberId = repository.findByMemberIdAndLocalDate(memberId,
            LocalDate.now());
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

        Optional<RankingsVO> rankingVOFindByYear = browseRanking(LogPeriod.YEAR);
        Optional<RankingsVO> rankingVOFindByMonth = browseRanking(LogPeriod.MONTH);
        Optional<RankingsVO> rankingVOFindByWeek = browseRanking(LogPeriod.WEEK);

        Map<Long, List<Long>> rankingYear = calcRanking(memberIds, LogPeriod.YEAR);
        Map<Long, List<Long>> rankingMonth = calcRanking(memberIds, LogPeriod.MONTH);
        Map<Long, List<Long>> rankingWeek = calcRanking(memberIds, LogPeriod.WEEK);

        if (rankingVOFindByYear.isEmpty()
            || rankingVOFindByMonth.isEmpty()
            || rankingVOFindByWeek.isEmpty()
        ) {
            saveRanking(rankingYear, LogPeriod.YEAR);
            saveRanking(rankingMonth, LogPeriod.MONTH);
            saveRanking(rankingWeek, LogPeriod.WEEK);
            return;
        }

        RankingsVO rankingVOYear = rankingVOFindByYear.get();
        RankingsVO rankingVOMonth = rankingVOFindByMonth.get();
        RankingsVO rankingVOWeek = rankingVOFindByWeek.get();

        saveRanking(rankingVOYear.compareRanking(rankingYear), LogPeriod.YEAR);
        saveRanking(rankingVOMonth.compareRanking(rankingMonth), LogPeriod.MONTH);
        saveRanking(rankingVOWeek.compareRanking(rankingWeek), LogPeriod.WEEK);
    }

    private void saveRanking(Map<Long, List<Long>> ranking, LogPeriod period) {
        rankingsRepository.save(new RankingsVO(ranking, period));
    }

    public Optional<RankingsVO> browseRanking(LogPeriod period) {
        return rankingsRepository.findRecent(period)
            .stream()
            .max(Comparator.comparing(RankingsVO::getLocalDateTime));
    }

    private Map<Long, List<Long>> calcRanking(List<Long> memberIds, LogPeriod period) {
        Map<Long, List<Long>> visitCountMemberIds = calcVisitCountMemberIds(memberIds, period);
        return rankingOrderByVisitCount(visitCountMemberIds);
    }

    private Map<Long, List<Long>> rankingOrderByVisitCount(Map<Long, List<Long>> visitCountMemberIds) {
        List<Long> reversedVisitCount = visitCountMemberIds.keySet().stream()
            .sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        Map<Long, List<Long>> ranking = new HashMap<>();
        Long rank = 1L;
        for (Long visitCount : reversedVisitCount) {
            ranking.put(rank, visitCountMemberIds.get(visitCount));
            rank++;
        }

        return ranking;
    }

    private Map<Long, List<Long>> calcVisitCountMemberIds(List<Long> memberIds, LogPeriod period) {
        return memberIds.stream()
            .map((Long id)
                -> new MemberVisitCountVO(
                id,
                repository.countByMemberIdAndLocalDateBetween(
                    id,
                    period.getBeforeLocalDate(), LocalDate.now())
            ))
            .collect(Collectors.groupingBy(
                    MemberVisitCountVO::getVisitCount,
                    Collectors.mapping(
                        MemberVisitCountVO::getMemberId,
                        Collectors.toList())
                )
            );
    }

    @Override
    public Long calcMemberRanking(Map<Long, List<Long>> rankings, Long memberId) {
        return rankings.entrySet()
            .stream()
            .filter(r -> r.getValue()
                .contains(memberId))
            .findFirst()
            .get()
            .getKey();
    }

    @Override
    public Long browseMemberVisitCount(Long memberId, LogPeriod type) {
        return repository.countByMemberIdAndLocalDateBetween(memberId,
            type.getBeforeLocalDate(), LocalDate.now());
    }
}
