package com.example.iptimeAPI.service.clubRoom;

import com.example.iptimeAPI.domain.clubRoom.ClubRoomLog;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogRepository;
import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import com.example.iptimeAPI.web.dto.MemberRankingDTO;
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
    public List<MemberRankingDTO> getRanking(List<Long> memberIds, RankingType type) {

        Map<Long, Long> memberVisitCount = getMemberVisitCountResult(memberIds, type);

        Map<Long, List<Long>> memberVisitCountResult = calculateMemberVisitCount(memberVisitCount);

        List<List<Long>> rankingResults = convertToRanking(memberVisitCountResult);

        return convertToDTOs(rankingResults);
    }

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


    private Map<Long, List<Long>> calculateMemberVisitCount(Map<Long, Long> memberOrderByVisitCount) {
        Map<Long, List<Long>> calculatedMemberVisitCountResult = calculateMemberVisitCountResult(memberOrderByVisitCount);

        return orderByCount(calculatedMemberVisitCountResult);
    }

    private Map<Long, List<Long>> calculateMemberVisitCountResult(Map<Long, Long> memberOrderByVisitCount) {
        Map<Long, List<Long>> calculatedRankingResult = new HashMap<>();
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

    private Map<Long, List<Long>> orderByCount(Map<Long, List<Long>> calculatedRankingResult) {
        Map<Long, List<Long>> reverseByKey = new TreeMap<>(Comparator.reverseOrder());
        reverseByKey.putAll(calculatedRankingResult);
        return reverseByKey;
    }

    private List<List<Long>> convertToRanking(Map<Long, List<Long>> rankingResultOrderByVisitCount) {
        return rankingResultOrderByVisitCount.values()
                .stream()
                .peek(Collections::shuffle)
                .collect(Collectors.toList());
    }

    private List<MemberRankingDTO> convertToDTOs(List<List<Long>> rankingAndMemberList) {
        List<MemberRankingDTO> memberRankingDTOS = new ArrayList<>();
        for (int i = 0, j = 1; i < rankingAndMemberList.size(); i++, j++) {
            for (Long memberId : rankingAndMemberList.get(i)) {
                memberRankingDTOS.add(new MemberRankingDTO(j, memberId));
            }
        }
        return memberRankingDTOS;
    }
}
