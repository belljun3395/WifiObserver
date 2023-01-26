package com.example.iptimeAPI.domain.clubRoom;

import com.example.iptimeAPI.service.clubRoom.MemberVisitCountVO;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubRoomLogRepository extends JpaRepository<ClubRoomLog, Long> {

    Long countByMemberIdAndLocalDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);

    Optional<ClubRoomLog> findByMemberIdAndLocalDate(Long memberId, LocalDate localDate);

    @Query("select new com.example.iptimeAPI.service.clubRoom.MemberVisitCountVO(c.memberId, count(c.memberId)) from ClubRoomLog c where c.localDate between :startDate and :endDate GROUP BY c.memberId")
    List<MemberVisitCountVO> countMemberVisitCountLocalDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
