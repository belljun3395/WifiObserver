package com.example.iptimeAPI.config.iptime;

import com.example.iptimeAPI.config.iptime.info.IptimeInfoConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * iptime 설정페이지에 보내는 HTTP 요청에 필요한 요소를 설정하는 클래스입니다.
 */
@Getter
@Component
@RequiredArgsConstructor
@PropertySource("classpath:iptime-http.properties")
public class IptimeHTTPConfig {
    private final IptimeInfoConfig iptimeInfoConfig;

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


    public String getValueOfCookieValueReferer() {
        return iptimeInfoConfig.getHostUrl() + login_session;
    }

    public String getValueOfCookieValue() {
        return iptimeInfoConfig.getHostUrl() + login_handler;
    }

    public String getValueOfListUrl() {
        return iptimeInfoConfig.getHostUrl() + login_timepro_query;
    }

    public String getValueOfListReferer() {
        return iptimeInfoConfig.getHostUrl() + login_handler;
    }
}
