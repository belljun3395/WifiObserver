package com.example.iptimeAPI.config.iptime;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class IptimeConfigAdmin {

    private final IptimeConfigHTTP iptimeConfigHTTP;
    private final IptimeConfig iptimeConfig;


    public Map<String, String> getLoginData() {
        Map<String, String> data = new HashMap<>();
        data.put("init_status", iptimeConfigHTTP.getInit_status());
        data.put("captcha_on", iptimeConfigHTTP.getCaptcha_on());
        data.put("username", iptimeConfig.getUsername());
        data.put("passwd", iptimeConfig.getPasswd());
        return data;
    }

}
