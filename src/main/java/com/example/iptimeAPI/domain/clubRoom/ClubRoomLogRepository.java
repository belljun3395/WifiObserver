package com.example.iptimeAPI.domain.clubRoom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ClubRoomLogRepository extends JpaRepository<ClubRoomLog, Long> {
        List<ClubRoomLog> findAllByMemberIdAndLocalDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);

}
