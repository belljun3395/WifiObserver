package com.example.iptimeAPI.domain.clubRoom;

import com.example.iptimeAPI.service.clubRoom.LogPeriod;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ClubRoomLogService {

    boolean save(Long memberId);

    Optional<RankingsVO> browseRanking(LogPeriod period);

    Long calcMemberRanking(Map<Long, List<Long>> rankings, Long memberId);

    Long browseMemberVisitCount(Long memberId, LogPeriod type);
}
