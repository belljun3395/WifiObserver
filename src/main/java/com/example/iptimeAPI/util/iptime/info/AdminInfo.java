package com.example.iptimeAPI.util.iptime.info;

import com.example.iptimeAPI.util.iptime.config.Const;

import java.util.HashMap;
import java.util.Map;

public class AdminInfo {

    public static Map<String, String> makeLoginData() {
        Map<String, String> data = new HashMap<>();
        data.put("init_status", Const.Init_Status);
        data.put("captcha_on", Const.Captcha_On);
        data.put("username", Const.Username);
        data.put("passwd", Const.Passwd);
        return data;
    }

}
