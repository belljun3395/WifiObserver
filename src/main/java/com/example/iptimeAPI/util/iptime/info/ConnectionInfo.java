package com.example.iptimeAPI.util.iptime.info;

import com.example.iptimeAPI.util.iptime.config.*;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;

import java.io.IOException;

import org.jsoup.Connection.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConnectionInfo {

    private final IptimeConfig iptimeConfig;
    private final IptimeConfigHTTP iptimeConfigHTTP;
    private final AdminInfo adminInfo;


    private CommonSetting getCommonSetting() {
        return CommonSetting.builder()
                .agent(iptimeConfigHTTP.getUseragent())
                .accept(iptimeConfigHTTP.getAccept())
                .accept_encoding(iptimeConfigHTTP.getAccept_encoding())
                .accept_language(iptimeConfigHTTP.getAccept_language())
                .cache_control(iptimeConfigHTTP.getCache_control())
                .connection(iptimeConfigHTTP.getConnection())
                .host(iptimeConfig.getHost())
                .origin(iptimeConfig.getOrigin())
                .upgrade_insecure_request(iptimeConfigHTTP.getUpgrade_insecure_request())
                .build();
    }


    public Response getCookieValueConnection() throws IOException {
        return connect(iptimeConfigHTTP.get_cookie_value(), Method.POST, getCommonSetting(), iptimeConfigHTTP.get_cookie_value_referer(), iptimeConfigHTTP.getContent_length(), iptimeConfigHTTP.getContent_type());
    }

    public Response login(String cookie_value) throws IOException {
        return connect(iptimeConfigHTTP.get_login_url(), Method.GET, getCommonSetting(), iptimeConfigHTTP.get_login_referer(), cookie_value, iptimeConfigHTTP.getContent_length(), iptimeConfigHTTP.getContent_type());
    }

    public Response getList(String cookie_value) throws IOException {
        return connect(iptimeConfigHTTP.get_list_url(), Method.GET, getCommonSetting(), iptimeConfigHTTP.get_list_referer(), cookie_value);
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
                .data(adminInfo.getLoginData())
                .execute();
    }

    private Response connect(String url, Method method, CommonSetting commonSetting, String referer, String cookie_value, String content_length, String content_type) throws IOException {
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
                .header("Content-Length", content_length)
                .header("Content-Type", content_type)
                .data(adminInfo.getLoginData())
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
                .data(adminInfo.getLoginData())
                .execute();
    }
}
