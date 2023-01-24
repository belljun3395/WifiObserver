package com.example.iptimeAPI.domain.clubRoom;

import com.example.iptimeAPI.service.clubRoom.LogPeriod;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("ranking")
@Getter
public class RankingsVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Enumerated
    @AccessType(AccessType.Type.PROPERTY)
    private LogPeriod period;
    private Map<Long, List<Long>> rankings;

    private LocalDateTime localDateTime;

    public RankingsVO(Map<Long, List<Long>> rankings, LogPeriod period) {
        this.rankings = rankings;
        this.period = period;
        this.localDateTime = LocalDateTime.now();
    }

    public Map<Long, List<Long>> compareRanking(Map<Long, List<Long>> compareRanking) {
        Set<Entry<Long, List<Long>>> compareRankings = compareRanking.entrySet();
        for (Entry<Long, List<Long>> ranking : compareRankings) {
            if (!this.rankings.containsValue(ranking.getValue())) {
                this.rankings = compareRanking;
                break;
            }
        }
        return this.rankings;
    }
}
