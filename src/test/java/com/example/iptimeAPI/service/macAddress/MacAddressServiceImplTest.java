package com.example.iptimeAPI.service.macAddress;

import com.example.iptimeAPI.controller.dto.MacAddressDTO;
import com.example.iptimeAPI.domain.macAddress.MacAddress;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;


@SpringBootTest
class MacAddressServiceImplTest {


    @Autowired
    private MacAddressService macAddressService;


    @Test
    public void browseMacAddresses() {
        List<MacAddress> macAddresses = macAddressService.browseMacAddresses();
        Assertions.assertThat(macAddresses.size())
                .isEqualTo(4);
    }

    @Test
    public void browseExistMember() throws IOException {
        List<MacAddressDTO> macAddressDTOS = macAddressService.browseExistMember();
        Assertions.assertThat(macAddressDTOS.size())
                .isEqualTo(4);
    }

    @Test
    public void validateRegistedMember() {
        macAddressService.validateRegistedMember(1L);
    }

    @Test
    public void validateRegistedMember_Exception() {
        Assertions.assertThatIllegalStateException()
                .isThrownBy(() -> macAddressService.validateRegistedMember(5L));
    }

    @Test
    public void checkMemberMacAddressIsExist() throws IOException {
        MacAddress macAddress = new MacAddress(5L, "2C-3A-E8-3E-82-BC");

        Assertions.assertThatIllegalStateException()
                .isThrownBy(() -> macAddressService.checkMemberMacAddressIsExist(macAddress));
    }

    @Test
    public void browseRegistedMembers() {
        List<Long> members = macAddressService.browseRegistedMembers();

        Assertions.assertThat(members.size())
                .isEqualTo(4);
    }
}