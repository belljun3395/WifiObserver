package com.example.iptimeAPI.domain.clubRoom;

import com.example.iptimeAPI.service.clubRoom.RankingType;
import com.example.iptimeAPI.web.dto.MemberRankingDTO;
import com.example.iptimeAPI.web.fegin.UserInfo;

import java.util.List;

public interface ClubRoomLogService {

    void save(Long memberId);

    List<MemberRankingDTO> getRanking(List<UserInfo> userInfos, RankingType type);


}
