package com.example.iptimeAPI.service.clubRoom;


import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ClubRoomLogServiceImplTest {

    @Autowired
    private ClubRoomLogServiceImpl clubRoomLogService;

    @Test
    void save_empty() {
        String returnValue = clubRoomLogService.save_test(1L);

        Assertions.assertThat(returnValue).isEqualTo("empty and save");
    }

    @Test
    void save_present() {
        clubRoomLogService.save_test(1L);

        String returnValue = clubRoomLogService.save_test(1L);

        Assertions.assertThat(returnValue).isEqualTo("present");
    }

    @Test
    void calcRanking_week() {
        Map<Long, Long> rankings = clubRoomLogService.calcRanking(LogPeriod.WEEK);

        Assertions.assertThat(rankings.get(1L)).isEqualTo(1L);
        Assertions.assertThat(rankings.get(2L)).isEqualTo(2L);
        Assertions.assertThat(rankings.get(3L)).isEqualTo(2L);
        Assertions.assertThat(rankings.get(4L)).isEqualTo(2L);

    }

    @Test
    void calcRanking_month() {
        Map<Long, Long> rankings = clubRoomLogService.calcRanking(LogPeriod.MONTH);

        Assertions.assertThat(rankings.get(1L)).isEqualTo(1L);
        Assertions.assertThat(rankings.get(2L)).isEqualTo(2L);
        Assertions.assertThat(rankings.get(3L)).isEqualTo(2L);
        Assertions.assertThat(rankings.get(4L)).isEqualTo(3L);

    }

    @Test
    void calcRanking_year() {
        Map<Long, Long> rankings = clubRoomLogService.calcRanking(LogPeriod.YEAR);

        Assertions.assertThat(rankings.get(1L)).isEqualTo(1L);
        Assertions.assertThat(rankings.get(2L)).isEqualTo(2L);
        Assertions.assertThat(rankings.get(3L)).isEqualTo(3L);
        Assertions.assertThat(rankings.get(4L)).isEqualTo(4L);

    }

    @Test
    void browseMemberVisitCount() {
        Long week = clubRoomLogService.browseMemberVisitCount(1L, LogPeriod.WEEK);
        Long month = clubRoomLogService.browseMemberVisitCount(1L, LogPeriod.MONTH);
        Long year = clubRoomLogService.browseMemberVisitCount(1L, LogPeriod.YEAR);

        Assertions.assertThat(week).isEqualTo(2L);
        Assertions.assertThat(month).isEqualTo(3L);
        Assertions.assertThat(year).isEqualTo(4L);
    }
}