package com.example.iptimeAPI.domain.clubRoom;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 동방 출석 기록을 관리하기 위한 엔티티입니다.
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "club_arrival_departure_list")
public class ClubRoomLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private LocalDate localDate;


    public ClubRoomLog(Long memberId, LocalDate localDate) {
        this.memberId = memberId;
        this.localDate = localDate;
    }

}
