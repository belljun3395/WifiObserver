package com.example.iptimeAPI.web.dto;

import lombok.Data;

@Data
public class MemberRankingDTO {
    private Integer ranking;
    private Long memberId;

    public MemberRankingDTO(Integer ranking, Long memberId) {
        this.ranking = ranking;
        this.memberId = memberId;
    }
}
