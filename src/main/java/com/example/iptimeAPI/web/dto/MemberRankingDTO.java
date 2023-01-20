package com.example.iptimeAPI.web.dto;

import com.example.iptimeAPI.service.user.dto.UserInfoVO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberRankingDTO {
    private Integer ranking;
    private UserInfoVO userInfoVO;
}
