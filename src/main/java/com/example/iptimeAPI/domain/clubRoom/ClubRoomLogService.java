package com.example.iptimeAPI.domain.clubRoom;

import com.example.iptimeAPI.service.clubRoom.LogPeriod;
import java.util.Map;

public interface ClubRoomLogService {

    void save(Long memberId);

    Map<Long, Long> calcRanking(LogPeriod logPeriod);

    Long browseMemberVisitCount(Long memberId, LogPeriod type);

}
