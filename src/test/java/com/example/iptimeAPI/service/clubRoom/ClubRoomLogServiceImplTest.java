package com.example.iptimeAPI.service.clubRoom;

import com.example.iptimeAPI.domain.macAddress.MacAddressService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class ClubRoomLogServiceImplTest {

    @Autowired
    private ClubRoomLogServiceImpl clubRoomLogService;

    @Autowired
    private MacAddressService macAddressService;

    @Test
    void calcRanking_example() {
        List<Long> members = macAddressService.browseMacAddressesMembers();
        Map<Long, List<Long>> rankings = clubRoomLogService.calcRanking_test(members, LogPeriod.MONTH);
        Set<Entry<Long, List<Long>>> entries = rankings.entrySet();

        for (Entry<Long, List<Long>> entry : entries) {
            Collections.shuffle(entry.getValue());
            log.info("member ranking = {}  id = {}", entry.getKey(),
                entry.getValue());
        }
    }

    @Test
    void calcRanking_week() {
        List<Long> members = macAddressService.browseMacAddressesMembers();
        Map<Long, List<Long>> rankings = clubRoomLogService.calcRanking_test(members, LogPeriod.WEEK);

        List<Long> firstMembers = rankings.get(1L);
        Assertions.assertThat(firstMembers.size()).isEqualTo(1L);
        List<Long> secondMembers = rankings.get(2L);
        Assertions.assertThat(secondMembers.size()).isEqualTo(3L);
    }

    @Test
    void calcRanking_month() {
        List<Long> members = macAddressService.browseMacAddressesMembers();
        Map<Long, List<Long>> rankings = clubRoomLogService.calcRanking_test(members, LogPeriod.MONTH);

        List<Long> firstMembers = rankings.get(1L);
        Assertions.assertThat(firstMembers.size()).isEqualTo(1L);
        List<Long> secondMembers = rankings.get(2L);
        Assertions.assertThat(secondMembers.size()).isEqualTo(2L);
        List<Long> thirdMembers = rankings.get(3L);
        Assertions.assertThat(thirdMembers.size()).isEqualTo(1L);
    }

    @Test
    void calcRanking_year() {
        List<Long> members = macAddressService.browseMacAddressesMembers();
        Map<Long, List<Long>> rankings = clubRoomLogService.calcRanking_test(members, LogPeriod.YEAR);

        List<Long> firstMembers = rankings.get(1L);
        Assertions.assertThat(firstMembers.size()).isEqualTo(1L);
        List<Long> secondMembers = rankings.get(2L);
        Assertions.assertThat(secondMembers.size()).isEqualTo(1L);
        List<Long> thirdMembers = rankings.get(3L);
        Assertions.assertThat(thirdMembers.size()).isEqualTo(1L);
        List<Long> fourthMembers = rankings.get(3L);
        Assertions.assertThat(fourthMembers.size()).isEqualTo(1L);
    }

    @Test
    void calcMemberRanking_week() {
        List<Long> members = macAddressService.browseMacAddressesMembers();
        Map<Long, List<Long>> rankings = clubRoomLogService.calcRanking_test(members, LogPeriod.WEEK);

        Long firstMemberRanking = clubRoomLogService.calcMemberRanking(rankings, 1L);
        Assertions.assertThat(firstMemberRanking).isEqualTo(1L);

        Long secondMemberRanking = clubRoomLogService.calcMemberRanking(rankings, 2L);
        Assertions.assertThat(secondMemberRanking).isEqualTo(2L);

        Long thirdMemberRanking = clubRoomLogService.calcMemberRanking(rankings, 3L);
        Assertions.assertThat(thirdMemberRanking).isEqualTo(2L);

        Long fourthMemberRanking = clubRoomLogService.calcMemberRanking(rankings, 4L);
        Assertions.assertThat(fourthMemberRanking).isEqualTo(2L);
    }

    @Test
    void calcMemberRanking_month() {
        List<Long> members = macAddressService.browseMacAddressesMembers();
        Map<Long, List<Long>> rankings = clubRoomLogService.calcRanking_test(members, LogPeriod.MONTH);

        Long firstMemberRanking = clubRoomLogService.calcMemberRanking(rankings, 1L);
        Assertions.assertThat(firstMemberRanking).isEqualTo(1L);

        Long secondMemberRanking = clubRoomLogService.calcMemberRanking(rankings, 2L);
        Assertions.assertThat(secondMemberRanking).isEqualTo(2L);

        Long thirdMemberRanking = clubRoomLogService.calcMemberRanking(rankings, 3L);
        Assertions.assertThat(thirdMemberRanking).isEqualTo(2L);

        Long fourthMemberRanking = clubRoomLogService.calcMemberRanking(rankings, 4L);
        Assertions.assertThat(fourthMemberRanking).isEqualTo(3L);
    }

    @Test
    void calcMemberRanking_year() {
        List<Long> members = macAddressService.browseMacAddressesMembers();
        Map<Long, List<Long>> rankings = clubRoomLogService.calcRanking_test(members, LogPeriod.YEAR);

        Long firstMemberRanking = clubRoomLogService.calcMemberRanking(rankings, 1L);
        Assertions.assertThat(firstMemberRanking).isEqualTo(1L);

        Long secondMemberRanking = clubRoomLogService.calcMemberRanking(rankings, 2L);
        Assertions.assertThat(secondMemberRanking).isEqualTo(2L);

        Long thirdMemberRanking = clubRoomLogService.calcMemberRanking(rankings, 3L);
        Assertions.assertThat(thirdMemberRanking).isEqualTo(3L);

        Long fourthMemberRanking = clubRoomLogService.calcMemberRanking(rankings, 4L);
        Assertions.assertThat(fourthMemberRanking).isEqualTo(4L);
    }

    @Test
    void browseMemberVisitCount() {
        Long weekCount = clubRoomLogService.browseMemberVisitCount(1L, LogPeriod.WEEK);
        Long monthCount = clubRoomLogService.browseMemberVisitCount(1L, LogPeriod.MONTH);
        Long yearCount = clubRoomLogService.browseMemberVisitCount(1L, LogPeriod.YEAR);

        Assertions.assertThat(weekCount).isEqualTo(2L);
        Assertions.assertThat(monthCount).isEqualTo(3L);
        Assertions.assertThat(yearCount).isEqualTo(4L);
    }
}