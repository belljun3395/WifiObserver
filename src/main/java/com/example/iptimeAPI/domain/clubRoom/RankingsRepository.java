package com.example.iptimeAPI.domain.clubRoom;

import com.example.iptimeAPI.service.clubRoom.LogPeriod;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RankingsRepository extends MongoRepository<RankingsVO, String> {

    @Query("{'period' : ?0}")
    List<RankingsVO> findRecent(LogPeriod logPeriod);

}
