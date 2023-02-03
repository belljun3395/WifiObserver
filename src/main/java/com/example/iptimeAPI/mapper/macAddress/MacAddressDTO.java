package com.example.iptimeAPI.mapper.macAddress;

import lombok.Getter;

@Getter
public class MacAddressDTO {

    private Long id;

    private Long memberId;

    private String macAddress;


    public MacAddressDTO(Long id, Long memberId, String macAddress) {
        this.id = id;
        this.memberId = memberId;
        this.macAddress = macAddress;
    }
}
