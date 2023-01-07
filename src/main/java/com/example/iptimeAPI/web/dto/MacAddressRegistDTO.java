package com.example.iptimeAPI.web.dto;

import com.example.iptimeAPI.domain.macAddress.MacAddress;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class MacAddressRegistDTO {

    @ApiParam(example = "5")
    private Long memberId;

    @ApiParam(example = "6A:3C:48:3A:22:60")
    private String macAddress;

    public MacAddressRegistDTO(Long memberId, String macAddress) {
        this.memberId = memberId;
        this.macAddress = macAddress;
    }
}
