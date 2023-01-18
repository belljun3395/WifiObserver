package com.example.iptimeAPI.web.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class IpDTO {
    @ApiParam(example = "168.131.35.101")
    @Pattern(regexp = "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])", message = "IP 주소 형식이 올바르지 않습니다")
    private String ip;
}