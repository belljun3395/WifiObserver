package com.example.iptimeAPI.domain.clubRoom;

import com.example.iptimeAPI.service.clubRoom.LogPeriod;
import java.util.Map;

/**
 * 동방과 관련된 서비스를 위한 인터페이스입니다.
 */
public interface ClubRoomLogService {

    /**
     * 동방 방문을 기록하는 기능입니다.
     * @param memberId 동방에 방문한 멤버 id
     */
    void save(Long memberId);

    /**
     * 조회하려는 기간동안의 멤버들의 동방 방문 랭킹을 구하는 기능입니다.
     * @param logPeriod 조회하려는 기간
     * @return 조회하려는 기간동안의 멤버들의 동방 방문 랭킹
     */
    Map<Long, Long> calcRanking(LogPeriod logPeriod);

    /**
     * 조회하려는 기간동안의 멤버의 동방 방문 횟수를 구하는 기능입니다.
     * @param memberId 멤버 id
     * @param type 조회하려는 기간
     * @return 조회하려는 기간동안의 멤버의 동방 방문 횟수
     */
    Long browseMemberVisitCount(Long memberId, LogPeriod type);

}
