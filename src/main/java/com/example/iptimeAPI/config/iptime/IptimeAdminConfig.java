package com.example.iptimeAPI.config.iptime;

import com.example.iptimeAPI.config.iptime.info.IptimeInfoConfig;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * iptime 설정페이지에서 로그인을 위한 정보를 설정하는 클래스 입ㄴ디ㅏ.
 */
@Component
@RequiredArgsConstructor
public class IptimeAdminConfig {

    private final IptimeHTTPConfig iptimeHTTPConfig;
    private final IptimeInfoConfig iptimeInfoConfig;


    public Map<String, String> getLoginData() {
        Map<String, String> data = new HashMap<>();
        data.put("init_status", iptimeHTTPConfig.getInit_status());
        data.put("captcha_on", iptimeHTTPConfig.getCaptcha_on());
        data.put("username", iptimeInfoConfig.getUsername());
        data.put("passwd", iptimeInfoConfig.getPasswd());
        return data;
    }

}
