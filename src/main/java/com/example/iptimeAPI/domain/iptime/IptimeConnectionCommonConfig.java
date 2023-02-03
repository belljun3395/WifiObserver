package com.example.iptimeAPI.domain.iptime;

import com.example.iptimeAPI.config.iptime.IptimeHTTPConfig;
import com.example.iptimeAPI.config.iptime.info.IptimeInfoConfig;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class IptimeConnectionCommonConfig {

    private String agent;
    private String accept;
    private String accept_encoding;
    private String accept_language;
    private String cache_control;
    private String connection;
    private String host;
    private String origin;
    private String upgrade_insecure_request;


    public IptimeConnectionCommonConfig(IptimeHTTPConfig iptimeHTTPConfig, IptimeInfoConfig iptimeInfoConfig) {
        this.agent = iptimeHTTPConfig.getUseragent();
        this.accept = iptimeHTTPConfig.getAccept();
        this.accept_encoding = iptimeHTTPConfig.getAccept_encoding();
        this.accept_language = iptimeHTTPConfig.getAccept_language();
        this.cache_control = iptimeHTTPConfig.getCache_control();
        this.connection = iptimeHTTPConfig.getConnection();
        this.host = iptimeInfoConfig.getHost();
        this.origin = iptimeInfoConfig.getOrigin();
        this.upgrade_insecure_request = iptimeHTTPConfig.getUpgrade_insecure_request();
    }

}
