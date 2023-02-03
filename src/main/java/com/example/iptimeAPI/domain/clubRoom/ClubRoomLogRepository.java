package com.example.iptimeAPI.domain.clubRoom;

import com.example.iptimeAPI.repository.clubRoom.MemberVisitCountVO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubRoomLogRepository extends JpaRepository<ClubRoomLog, Long> {

    /**
     * memberId를 기준으로 일정 시간 사이의 방문기록을 조회하기 위한 메서드입니다.
     * @param memberId member의 id 값입니다.
     * @param startDate 조회하려는 기간의 시작 날자를 설정합니다.
     * @param endDate 조회하려는 기간의 끝 날자를 설정합니다.
     * @return 조회한 기간 사이의 멤버의 동방 방문횟수
     */
    Long countByMemberIdAndLocalDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);

    /**
     * memberId의 날자별를 기준으로 방문 여부를 조회하기 위한 메서드입니다.
     * @param memberId member의 id 값입니다.
     * @param localDate 조회하려는 날자를 설정합니다.
     * @return 조회한 날자의 멤버의 동방 방문 기록
     */
    Optional<ClubRoomLog> findByMemberIdAndLocalDate(Long memberId, LocalDate localDate);

    /**
     * 조회하려는 기간을 기준으로 멤버들의 동방 방문횟수를 조회하가 위한 메서드이다.
     * @param startDate 조회하려는 기간의 시작 날자를 설정합니다.
     * @param endDate 조회하려는 기간의 끝 날자를 설정합니다.
     * @return 조회하려는 기간의 멤버들의 동방 방문횟수
     */
    @Query(
        "select new com.example.iptimeAPI.repository.clubRoom.MemberVisitCountVO(c.memberId, count(c.memberId)) "
            + "from ClubRoomLog c "
            + "where c.localDate between :startDate and :endDate GROUP BY c.memberId")
    List<MemberVisitCountVO> countMemberVisitCountLocalDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
