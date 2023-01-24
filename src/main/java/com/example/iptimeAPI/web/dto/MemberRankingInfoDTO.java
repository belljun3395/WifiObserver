package com.example.iptimeAPI.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberRankingInfoDTO {

    private Long year;
    private String name;
    private Long id;
    private Long ranking;
    private Long visitCount;
}
