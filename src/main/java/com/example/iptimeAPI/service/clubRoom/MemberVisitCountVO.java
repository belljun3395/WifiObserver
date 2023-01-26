package com.example.iptimeAPI.service.clubRoom;

import lombok.Getter;

@Getter
public class MemberVisitCountVO {

    private Long memberId;
    private Long visitCount;

    public MemberVisitCountVO(Long memberId, Long visitCount) {
        this.memberId = memberId;
        this.visitCount = visitCount;
    }
}
