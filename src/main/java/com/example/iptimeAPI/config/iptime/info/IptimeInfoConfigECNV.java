package com.example.iptimeAPI.config.iptime.info;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@PropertySource("classpath:iptime-ecnv.properties")
public class IptimeInfoConfigECNV implements IptimeInfoConfig {

    @Value("${ecnv.host.url}")
    private String hosturl;

    @Value("${ecnv.host}")
    private String host;

    @Value("${ecnv.origin}")
    private String origin;

    @Value("${ecnv.passwd}")
    private String passwd;

    @Value("${ecnv.username}")
    private String username;

    @Value("${ecnv.ip}")
    private String ip;


    public boolean isIp(String ip) {
        return this.ip.equals(ip);
    }

}
