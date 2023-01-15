package com.example.iptimeAPI.web.fegin;

import org.springframework.stereotype.Component;

@Component
public class FeignUserInfoFallbackImpl implements FeignUserInfo {

    @Override
    public UserInfo getUserInfo(Long userId) {
        return null;
    }
}
