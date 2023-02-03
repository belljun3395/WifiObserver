package com.example.iptimeAPI.web.dto;

import com.example.iptimeAPI.domain.user.UserInfoVO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberRankingCountResponse {

    private Long year;

    private String name;

    private Long id;

    private Long ranking;

    private Long visitCount;


    public MemberRankingCountResponse(UserInfoVO userInfoVO,
                                        Long ranking,
                                        Long visitCount) {

        this.year = userInfoVO.getYear();
        this.name = userInfoVO.getName();
        this.id = userInfoVO.getId();
        this.ranking = ranking;
        this.visitCount = visitCount;
    }

}
