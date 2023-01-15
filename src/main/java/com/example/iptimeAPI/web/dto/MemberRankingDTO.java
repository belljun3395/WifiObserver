package com.example.iptimeAPI.web.dto;

import com.example.iptimeAPI.web.fegin.UserInfo;
import lombok.Data;

@Data
public class MemberRankingDTO {
    private Integer ranking;
    private UserInfo userInfo;

    public MemberRankingDTO(Integer ranking, UserInfo userInfo) {
        this.ranking = ranking;
        this.userInfo = userInfo;
    }
}
