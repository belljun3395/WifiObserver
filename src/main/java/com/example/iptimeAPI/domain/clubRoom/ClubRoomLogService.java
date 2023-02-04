package com.example.iptimeAPI.domain.clubRoom;

import com.example.iptimeAPI.service.clubRoom.LogPeriod;
import java.util.Map;

public interface ClubRoomLogService {

    /**
     * 동방 방문을 기록하는 기능입니다.
     * @param memberId 동방에 방문한 member의 id
     */
    void save(Long memberId);

    /**
     * @param logPeriod 조회 기간 타입
     * @return 조회 기간 타입 동안의 전체 member의 동방 방문 랭킹
     */
    Map<Long, Long> calcRanking(LogPeriod logPeriod);

    /**
     * @param memberId member의 id
     * @param type 조회 기간 타입
     * @return 조회 기간 타입 동안의 member의 동방 방문 횟수
     */
    Long browseMemberVisitCount(Long memberId, LogPeriod type);

}
