package com.example.iptimeAPI.config.iptime;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Profile("!dev")
@Component
@PropertySource("classpath:iptime-ecnv.properties")
public class IptimeConfigECNV implements IptimeConfig {

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

}
