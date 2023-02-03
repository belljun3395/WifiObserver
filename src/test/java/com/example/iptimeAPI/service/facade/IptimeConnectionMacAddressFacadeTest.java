package com.example.iptimeAPI.service.facade;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class IptimeConnectionMacAddressFacadeTest {

    @Autowired
    private IptimeMacAddressFacade iptimeMacAddressFacade;

    @Test
    void browseExistMembers() {
        // 항상 있는 ip가 변경될 수 있음, 확인 필요
        List<Long> members = iptimeMacAddressFacade.browseExistMembers();
        Assertions.assertThat(members.size()).isEqualTo(4L);
    }
}