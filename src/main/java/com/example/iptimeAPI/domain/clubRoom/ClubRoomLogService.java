package com.example.iptimeAPI.domain.clubRoom;

import java.util.List;

public interface ClubRoomLogService {

    void addToDB(Long memberId);

    List<List<Long>> calculateRanking(List<Long> memberIds);

}
