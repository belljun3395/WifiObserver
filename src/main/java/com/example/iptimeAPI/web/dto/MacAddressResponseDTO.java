package com.example.iptimeAPI.web.dto;

import lombok.Data;

@Data
public class MacAddressResponseDTO {

    private Long id;

    private Long memberId;

    private String macAddress;

    public MacAddressResponseDTO(Long id, Long memberId, String macAddress) {
        this.id = id;
        this.memberId = memberId;
        this.macAddress = macAddress;
    }
}
