package com.example.iptimeAPI.web.dto;

import com.example.iptimeAPI.domain.macAddress.MacAddress;
import lombok.Data;

@Data
public class MacAddressRegistDTO {

    private Long memberId;

    private String macAddress;

    public MacAddressRegistDTO(Long memberId, String macAddress) {
        this.memberId = memberId;
        this.macAddress = macAddress;
    }

    public MacAddress convertToMacAddress() {
        return new MacAddress(memberId, macAddress);
    }
}
