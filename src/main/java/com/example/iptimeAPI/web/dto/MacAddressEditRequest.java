package com.example.iptimeAPI.web.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MacAddressEditRequest {

    @ApiParam(example = "1")
    private Long id;

    @ApiParam(example = "1")
    private Long memberId;

    @ApiParam(example = "90:32:4B:18:00:1B")
    private String macAddress;

}
