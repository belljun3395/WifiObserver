package com.example.iptimeAPI.config.iptime.info;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@PropertySource("classpath:iptime.properties")
public class IptimeInfoConfigECNV implements IptimeInfoConfig {
    @Value("${iptime.host.url}")
    private String hostUrl;

    @Value("${iptime.host}")
    private String host;

    @Value("${iptime.origin}")
    private String origin;

    @Value("${iptime.passwd}")
    private String passwd;

    @Value("${iptime.username}")
    private String username;

    @Value("${iptime.ip}")
    private String ip;


    public boolean isIp(String ip) {
        return this.ip.equals(ip);
    }
}
