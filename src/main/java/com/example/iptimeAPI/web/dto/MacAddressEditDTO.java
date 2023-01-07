package com.example.iptimeAPI.web.dto;

import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class MacAddressEditDTO {
    @ApiParam(example = "1")
    private Long id;

    @ApiParam(example = "1")
    private Long memberId;

    @ApiParam(example = "90:32:4B:18:00:1B")
    private String macAddress;

    public MacAddressEditDTO(Long id, Long memberId, String macAddress) {
        this.id = id;
        this.memberId = memberId;
        this.macAddress = macAddress;
    }
}
