package com.example.iptimeAPI.config.iptime;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Profile("dev")
@Component
@PropertySource("classpath:iptime-dev.properties")
public class IptimeConfigDEV implements IptimeConfig {

    @Value("${dev.host.url}")
    private String hosturl;

    @Value("${dev.host}")
    private String host;

    @Value("${dev.origin}")
    private String origin;

    @Value("${dev.passwd}")
    private String passwd;

    @Value("${dev.username}")
    private String username;

    @Value("${dev.ip}")
    private String ip;
}
