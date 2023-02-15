package com.example.iptimeAPI.service.user.fegin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignUserInfoFactory implements FallbackFactory<FeignUserInfo> {
    @Override
    public FeignUserInfo create(Throwable cause) {
        log.error("error = [{}][{}]", cause.getCause(), cause.getMessage());
        return null;
    }
}
