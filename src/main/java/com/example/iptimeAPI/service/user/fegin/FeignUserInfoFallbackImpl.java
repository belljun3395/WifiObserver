package com.example.iptimeAPI.service.user.fegin;

import com.example.iptimeAPI.service.user.dto.UserInfoDTO;
import org.springframework.stereotype.Component;

@Component
public class FeignUserInfoFallbackImpl implements FeignUserInfo {

    @Override
    public UserInfoDTO getUserInfo(Long userId) {
        return null;
    }

    @Override
    public UserInfoDTO getUserInfoByToken(String accessToken) {
        return null;
    }
}
