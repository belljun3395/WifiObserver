package com.example.iptimeAPI.domain.iptime;

import com.example.iptimeAPI.config.iptime.IptimeAdminConfig;
import com.example.iptimeAPI.config.iptime.IptimeHTTPConfig;
import com.example.iptimeAPI.config.iptime.info.IptimeInfoConfig;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

/**
 * iptime 설정페이지에 연결하기 위한 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class IptimeConnection {
    private final IptimeInfoConfig iptimeInfoConfig;

    private final IptimeHTTPConfig iptimeHTTPConfig;

    private final IptimeAdminConfig iptimeAdminConfig;


    private final IptimeConnectionCommonConfig iptimeConnectionCommonConfig;


    public boolean isConnect(String ip) {
        return iptimeInfoConfig.isIp(ip);
    }

    public String getValueOfIp() {
        return iptimeInfoConfig.getIp();
    }

    public Response queryCookieValue() throws IOException {
        return connect(
            iptimeHTTPConfig.getValueOfCookieValue(),
            Method.POST,
            iptimeConnectionCommonConfig,
            iptimeHTTPConfig.getValueOfCookieValueReferer(),
            iptimeHTTPConfig.getContent_length(),
            iptimeHTTPConfig.getContent_type()
        );

    }

    public Response queryMacAddressList(String cookie_value) throws IOException {
        return connect(
            iptimeHTTPConfig.getValueOfListUrl(),
            Method.GET,
            iptimeConnectionCommonConfig,
            iptimeHTTPConfig.getValueOfListReferer(),
            cookie_value
        );

    }

    private Response connect(
                                String url,
                                Method method,
                                IptimeConnectionCommonConfig commonSetting,
                                String referer,
                                String content_length,
                                String content_type
                            ) throws IOException {
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
            .data(iptimeAdminConfig.getValueOfLoginData())
            .execute();
    }

    private Response connect(
                                String url,
                                Method method,
                                IptimeConnectionCommonConfig commonSetting,
                                String referer,
                                String cookie_value
                            ) throws IOException {
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
            .data(iptimeAdminConfig.getValueOfLoginData())
            .execute();
    }
}
