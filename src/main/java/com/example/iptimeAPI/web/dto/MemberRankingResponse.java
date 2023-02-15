package com.example.iptimeAPI.web.dto;

import com.example.iptimeAPI.domain.user.UserInfoVO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberRankingResponse {
    private Long ranking;

    private UserInfoVO userInfo;
}
