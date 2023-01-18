package com.example.iptimeAPI.web.dto;

import com.example.iptimeAPI.web.fegin.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberRankingDTO {
    private Integer ranking;
    private UserInfo userInfo;
}
