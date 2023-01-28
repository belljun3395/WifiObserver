package com.example.iptimeAPI.domain.user;

import com.example.iptimeAPI.service.user.dto.UserInfoVO;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "user")
public class User {

    private static final Long DEFAULT_TTL = 30L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Indexed
    private Long userId;

    private UserInfoVO userInfoVO;

    @TimeToLive
    private Long expiration = DEFAULT_TTL;

    public User(UserInfoVO userInfoVO) {
        this.userInfoVO = userInfoVO;
        this.userId = userInfoVO.getId();
    }
}
