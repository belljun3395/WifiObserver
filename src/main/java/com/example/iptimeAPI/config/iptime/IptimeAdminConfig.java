package com.example.iptimeAPI.config.iptime;

import com.example.iptimeAPI.config.iptime.info.IptimeInfoConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
