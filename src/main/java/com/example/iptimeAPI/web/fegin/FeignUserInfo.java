package com.example.iptimeAPI.web.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "user", url = "http://168.131.30.127:8080", fallbackFactory = FeignUserInfoFactory.class)
public interface FeignUserInfo {

    @RequestMapping(method = RequestMethod.GET, path = "/api/users/{userId}")
    UserInfo getUserInfo(@PathVariable("userId") Long userId);

    @RequestMapping(method = RequestMethod.GET, path = "/api/users/token")
    UserInfo getUserInfoByToken(@RequestHeader(name = "Authorization") String accessToken);

}
