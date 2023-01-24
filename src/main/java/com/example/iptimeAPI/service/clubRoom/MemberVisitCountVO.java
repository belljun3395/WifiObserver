package com.example.iptimeAPI.service.clubRoom;


public class MemberVisitCountVO {

    private Long memberId;
    private Long visitCount;

    public MemberVisitCountVO(Long memberId, Long visitCount) {
        this.memberId = memberId;
        this.visitCount = visitCount;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getVisitCount() {
        return visitCount;
    }
}
