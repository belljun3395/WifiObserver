package com.example.iptimeAPI.domain.clubRoom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClubRoomLogRepository extends JpaRepository<ClubRoomLog, Long> {
        Optional<ClubRoomLog> findByMemberIdAndLocalDate(Long memberId, LocalDate localDate);

        List<ClubRoomLog> findAllByMemberIdAndLocalDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);

        List<ClubRoomLog> findAllByMemberId(Long memberId);
}
