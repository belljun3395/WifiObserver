package com.example.iptimeAPI.web.dto;

import lombok.Data;

@Data
public class MacAddressEditDTO {
    private Long id;

    private Long memberId;

    private String macAddress;

    public MacAddressEditDTO(Long id, Long memberId, String macAddress) {
        this.id = id;
        this.memberId = memberId;
        this.macAddress = macAddress;
    }
}
