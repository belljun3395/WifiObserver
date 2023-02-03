package com.example.iptimeAPI.mapper.clubRoomLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class RankingMapper {

    public Map<Long, Long> groupByMemberId(Map<Long, List<Long>> groupByVisitCountRanking) {
        Set<Long> longs = groupByVisitCountRanking.keySet();

        Map<Long, Long> groupByMemberId = new HashMap<>();
        for (Long ranking : longs) {
            List<Long> memberIds = groupByVisitCountRanking.get(ranking);

            for (Long memberId : memberIds) {
                groupByMemberId.put(memberId, ranking);
            }

        }

        return groupByMemberId;
    }

}
