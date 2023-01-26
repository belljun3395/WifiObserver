package com.example.iptimeAPI.service.config.caching;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
public class CacheEvicts {

    @CacheEvict(value = "ranking", allEntries = true)
    public void evictRankingCache() {

    }

    @CacheEvict(value = "memberVisitCount", allEntries = true)
    public void evictMemberVisitCountCache() {

    }
}
