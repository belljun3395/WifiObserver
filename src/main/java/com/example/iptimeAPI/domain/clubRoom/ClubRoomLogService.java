package com.example.iptimeAPI.domain.clubRoom;

import java.util.List;

public interface ClubRoomLogService {

    void save(Long memberId);

    List<List<Long>> calculateRanking(List<Long> memberIds);

}
