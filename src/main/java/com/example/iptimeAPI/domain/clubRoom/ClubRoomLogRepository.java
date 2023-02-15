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
     * @param memberId member의 id
     * @param startDate 시작 날짜
     * @param endDate 끝 날짜
     * @return 조회한 기간 사이의 member의 동방 방문횟수
     */
    Long countByMemberIdAndLocalDateBetween(Long memberId,
                                            LocalDate startDate,
                                            LocalDate endDate);

    /**
     * @param memberId member의 id
     * @param localDate 날짜
     * @return 조회 날짜의 멤버의 동방 방문 기록
     */
    Optional<ClubRoomLog> findByMemberIdAndLocalDate(Long memberId, LocalDate localDate);

    /**
     * @param startDate 시작 날짜
     * @param endDate 끝 날짜
     * @return 조회하려는 기간의 전체 member의 동방 방문횟수
     */
    @Query(
        "select new com.example.iptimeAPI.repository.clubRoom.MemberVisitCountVO(c.memberId, count(c.memberId)) "
            + "from ClubRoomLog c "
            + "where c.localDate between :startDate and :endDate GROUP BY c.memberId")
    List<MemberVisitCountVO> countMemberVisitCountLocalDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
