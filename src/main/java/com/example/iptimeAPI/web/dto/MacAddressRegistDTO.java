package com.example.iptimeAPI.web.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MacAddressRegistDTO {

    @ApiParam(example = "5")
    private Long memberId;

    @ApiParam(example = "6A:3C:48:3A:22:60")
    private String macAddress;
}
