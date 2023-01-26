package com.example.iptimeAPI.service.clubRoom;

import com.example.iptimeAPI.domain.clubRoom.ClubRoomLog;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogRepository;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import com.example.iptimeAPI.service.config.caching.CacheEvicts;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubRoomLogServiceImpl implements ClubRoomLogService {

    private final ClubRoomLogRepository repository;
    private final CacheEvicts cacheEvicts;

    @Override
    @Transactional
    public void save(Long memberId) {
        Optional<ClubRoomLog> byMemberId = repository.findByMemberIdAndLocalDate(memberId, LocalDate.now());

        if (byMemberId.isEmpty()) {
            repository.save(new ClubRoomLog(memberId, LocalDate.now()));
        }

        cacheEvicts.evictRankingCache();
        cacheEvicts.evictMemberVisitCountCache();
    }

    @Profile("test")
    @Transactional
    public String save_test(Long memberId) {
        Optional<ClubRoomLog> byMemberId = repository.findByMemberIdAndLocalDate(memberId, LocalDate.now());

        if (byMemberId.isEmpty()) {
            repository.save(new ClubRoomLog(memberId, LocalDate.now()));
            return "empty and save";
        }

        cacheEvicts.evictRankingCache();
        cacheEvicts.evictMemberVisitCountCache();

        return "present";
    }

    @Override
    @Cacheable(value = "ranking", key = "#logPeriod.type")
    public Map<Long, Long> calcRanking(LogPeriod logPeriod) {
        List<MemberVisitCountVO> memberVisitCountVOS =
            repository
                .countMemberVisitCountLocalDateBetween(
                    logPeriod.getBeforeLocalDate(),
                    LocalDate.now()
                );

        Map<Long, List<Long>> visitCountGroup = groupByVisitCount(memberVisitCountVOS);

        return RankingConverter.groupByMemberId(memberIdsRankOrderByVisitCount(visitCountGroup));
    }

    private Map<Long, List<Long>> groupByVisitCount(List<MemberVisitCountVO> memberVisitCountVOS) {
        return memberVisitCountVOS.stream()
            .collect(
                Collectors.groupingBy(
                    MemberVisitCountVO::getVisitCount,
                    Collectors.mapping(
                        MemberVisitCountVO::getMemberId,
                        Collectors.toList()
                    )
                )
            );
    }

    private Map<Long, List<Long>> memberIdsRankOrderByVisitCount(Map<Long, List<Long>> visitCountGroup) {
        List<Long> orderedVisitCount =
            visitCountGroup.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        Map<Long, List<Long>> rankings = new LinkedHashMap<>();
        Long rank = 1L;
        for (Long visitCount : orderedVisitCount) {
            rankings.put(rank, visitCountGroup.get(visitCount));
            rank++;
        }

        return rankings;
    }

    @Override
    @Cacheable(value = "memberVisitCount")
    public Long browseMemberVisitCount(Long memberId, LogPeriod type) {
        return repository.countByMemberIdAndLocalDateBetween(
            memberId,
            type.getBeforeLocalDate(),
            LocalDate.now());
    }
}
