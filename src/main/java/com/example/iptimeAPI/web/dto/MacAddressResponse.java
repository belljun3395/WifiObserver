package com.example.iptimeAPI.web.dto;

import com.example.iptimeAPI.mapper.macAddress.MacAddressDTO;

public class MacAddressResponse {
    private Long id;

    private Long memberId;

    private String macAddress;


    public MacAddressResponse(MacAddressDTO macAddressDTO) {
        this.id = macAddressDTO.getId();
        this.memberId = macAddressDTO.getMemberId();
        this.macAddress = macAddressDTO.getMacAddress();
    }
}
