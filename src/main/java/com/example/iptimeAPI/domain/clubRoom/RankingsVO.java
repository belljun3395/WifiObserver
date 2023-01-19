package com.example.iptimeAPI.domain.clubRoom;

import com.example.iptimeAPI.service.clubRoom.LogPeriod;
import lombok.Getter;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

}
