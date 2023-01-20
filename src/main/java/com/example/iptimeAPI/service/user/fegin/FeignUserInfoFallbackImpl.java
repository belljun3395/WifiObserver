package com.example.iptimeAPI.service.user.fegin;

import com.example.iptimeAPI.service.user.dto.UserInfoVO;
import org.springframework.stereotype.Component;

@Component
public class FeignUserInfoFallbackImpl implements FeignUserInfo {

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        return null;
    }

    @Override
    public UserInfoVO getUserInfoByToken(String accessToken) {
        return null;
    }
}
