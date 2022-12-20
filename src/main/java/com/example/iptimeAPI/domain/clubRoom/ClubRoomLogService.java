package com.example.iptimeAPI.domain.clubRoom;

import com.example.iptimeAPI.web.dto.MemberRankingDTO;

import java.util.List;

public interface ClubRoomLogService {

    void save(Long memberId);

    List<MemberRankingDTO> getRanking(List<Long> memberIds);

}
