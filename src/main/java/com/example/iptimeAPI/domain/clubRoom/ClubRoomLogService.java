package com.example.iptimeAPI.domain.clubRoom;

import com.example.iptimeAPI.service.clubRoom.LogPeriod;

import java.util.List;
import java.util.Map;

public interface ClubRoomLogService {

    boolean save(Long memberId);

    RankingsVO getRanking(LogPeriod period);

    Long calcRanking(Map<Long, List<Long>> rankings, Long memberId);

    Long calcVisitCount(Long memberId, LogPeriod type);
}
