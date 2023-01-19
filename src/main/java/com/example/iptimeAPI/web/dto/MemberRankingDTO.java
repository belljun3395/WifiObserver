package com.example.iptimeAPI.web.dto;

import com.example.iptimeAPI.service.user.dto.UserInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberRankingDTO {
    private Integer ranking;
    private UserInfoDTO userInfoDTO;
}
