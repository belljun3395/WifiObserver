package com.example.iptimeAPI.domain.iptime;

import com.example.iptimeAPI.config.iptime.IptimeAdminConfig;
import com.example.iptimeAPI.config.iptime.info.IptimeInfoConfig;
import com.example.iptimeAPI.config.iptime.IptimeHTTPConfig;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;

import java.io.IOException;

import org.jsoup.Connection.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IptimeConnection {

    private final IptimeInfoConfig iptimeInfoConfig;
    private final IptimeHTTPConfig iptimeHTTPConfig;
    private final IptimeAdminConfig iptimeAdminConfig;
    private CommonSetting commonsetting = CommonSetting.builder()
            .agent(iptimeHTTPConfig.getUseragent())
            .accept(iptimeHTTPConfig.getAccept())
            .accept_encoding(iptimeHTTPConfig.getAccept_encoding())
            .accept_language(iptimeHTTPConfig.getAccept_language())
            .cache_control(iptimeHTTPConfig.getCache_control())
            .connection(iptimeHTTPConfig.getConnection())
            .host(iptimeInfoConfig.getHost())
            .origin(iptimeInfoConfig.getOrigin())
            .upgrade_insecure_request(iptimeHTTPConfig.getUpgrade_insecure_request())
            .build();

    public boolean isConnect(String ip) {
        return iptimeInfoConfig.getIp()
                .equals(ip);
    }

    public Response getCookieValue() throws IOException {
        return connect(iptimeHTTPConfig.get_cookie_value(), Method.POST, commonsetting, iptimeHTTPConfig.get_cookie_value_referer(), iptimeHTTPConfig.getContent_length(), iptimeHTTPConfig.getContent_type());
    }

    public Response getList(String cookie_value) throws IOException {
        return connect(iptimeHTTPConfig.get_list_url(), Method.GET, commonsetting, iptimeHTTPConfig.get_list_referer(), cookie_value);
    }

    private Response connect(String url, Method method, CommonSetting commonSetting, String referer, String content_length, String content_type) throws IOException {
        return Jsoup.connect(url)
                .userAgent(commonSetting.getAgent())
                .header("Accept", commonSetting.getAccept())
                .header("Accept-Encoding", commonSetting.getAccept_encoding())
                .header("Accept-Language", commonSetting.getAccept_language())
                .header("Cache-Control", commonSetting.getCache_control())
                .header("Connection", commonSetting.getConnection())
                .header("Host", commonSetting.getHost())
                .header("Origin", commonSetting.getOrigin())
                .header("Upgrade-Insecure-Request", commonSetting.getUpgrade_insecure_request())
                .method(method)

                .header("Referer", referer)
                .header("Content-Length", content_length)
                .header("Content-Type", content_type)
                .data(iptimeAdminConfig.getLoginData())
                .execute();
    }

    private Response connect(String url, Method method, CommonSetting commonSetting, String referer, String cookie_value) throws IOException {
        return Jsoup.connect(url)
                .userAgent(commonSetting.getAgent())
                .header("Accept", commonSetting.getAccept())
                .header("Accept-Encoding", commonSetting.getAccept_encoding())
                .header("Accept-Language", commonSetting.getAccept_language())
                .header("Cache-Control", commonSetting.getCache_control())
                .header("Connection", commonSetting.getConnection())
                .header("Host", commonSetting.getHost())
                .header("Origin", commonSetting.getOrigin())
                .header("Upgrade-Insecure-Request", commonSetting.getUpgrade_insecure_request())
                .method(method)

                .header("Referer", referer)
                .cookie("efm_session_id", cookie_value)
                .data(iptimeAdminConfig.getLoginData())
                .execute();
    }

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
}
