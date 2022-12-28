package com.example.iptimeAPI.util.iptime.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
@PropertySource("classpath:iptime-http.properties")
public class IptimeConfigHTTP {

    private final IptimeConfig iptimeConfig;

    @Value("${http.useragent}")
    private String useragent;

    @Value("${http.accept}")
    private String accept;

    @Value("${http.accept.encoding}")
    private String accept_encoding;

    @Value("${http.accept.language}")
    private String accept_language;

    @Value("${http.cache.control}")
    private String cache_control;

    @Value("${http.content.length}")
    private String content_length;

    @Value("${http.connection}")
    private String connection;

    @Value("${http.content.type}")
    private String content_type;

    @Value("${http.upgrade.insecure.request}")
    private String upgrade_insecure_request;

    @Value("${http.login.session}")
    private String login_session;

    @Value("${http.login.handler}")
    private String login_handler;

    @Value("${http.login.query}")
    private String login;

    @Value("${http.login.timepro.query}")
    private String login_timepro_query;

    @Value("${http.init.status}")
    private String init_status;

    @Value("${http.captcha.on}")
    private String captcha_on;

    public String get_cookie_value_referer() {
        return iptimeConfig.getHosturl() + login_session;
    }

    public String get_cookie_value() {
        return iptimeConfig.getHosturl() + login_handler;
    }

    public String get_login_url() {
        return iptimeConfig.getHosturl() + login;
    }

    public String get_login_referer() {
        return iptimeConfig.getHosturl() + login_handler;
    }

    public String get_list_url() {
        return iptimeConfig.getHosturl() + login_timepro_query;
    }

    public String get_list_referer() {
        return iptimeConfig.getHosturl() + login_handler;
    }
}
