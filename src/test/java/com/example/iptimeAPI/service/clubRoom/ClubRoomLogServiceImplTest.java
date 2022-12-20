package com.example.iptimeAPI.service.clubRoom;

import com.example.iptimeAPI.domain.clubRoom.ClubRoomLogService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ClubRoomLogServiceImplTest {

    @Autowired
    private ClubRoomLogService clubRoomLogService;

    /*
    날짜에 따른 데이터 조정 필요
    (id, 유효 데이터, 비유효 데이터) = (1,1), (2,2,1), (3,1), (4,1)
     */
    @Test
    public void calculateRanking_1() {
        List<List<Long>> rankingList = clubRoomLogService.calculateRanking(List.of(1L, 2L, 3L, 4L));

        int secondSize = rankingList.get(1)
                .size(); // 1, 3, 4

        Assertions.assertThat(secondSize)
                .isEqualTo(3);
    }

    @Test
    public void calculateRanking_2() {
        List<List<Long>> rankingList = clubRoomLogService.calculateRanking(List.of(1L, 2L, 3L, 4L));

        List<Long> first = rankingList.get(0);
        Long firstMemberId = first.get(0);

        Assertions.assertThat(firstMemberId)
                .isEqualTo(2);
    }
}