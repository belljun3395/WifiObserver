package com.example.iptimeAPI.domain.clubRoom;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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
