package com.example.iptimeAPI.util.iptime.config;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonSetting {
    private String agent;
    private String accept;
    private String accept_encoding;
    private String accept_language;
    private String cache_control;
    private String connection;
    private String host;
    private String origin;
    private String upgrade_insecure_request;

    @Builder
    public CommonSetting(String agent, String accept, String accept_encoding, String accept_language, String cache_control, String connection, String host, String origin, String upgrade_insecure_request) {
        this.agent = agent;
        this.accept = accept;
        this.accept_encoding = accept_encoding;
        this.accept_language = accept_language;
        this.cache_control = cache_control;
        this.connection = connection;
        this.host = host;
        this.origin = origin;
        this.upgrade_insecure_request = upgrade_insecure_request;
    }

}
