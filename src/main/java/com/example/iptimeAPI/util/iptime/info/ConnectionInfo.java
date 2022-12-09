package com.example.iptimeAPI.util.iptime.info;

import com.example.iptimeAPI.util.iptime.config.CommonSetting;
import com.example.iptimeAPI.util.iptime.config.Const;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

import static org.jsoup.Connection.*;

public class ConnectionInfo {
    private static CommonSetting commonSetting = new CommonSetting(Const.UserAgent, Const.Accept,Const.Accept_Encoding,Const.Accept_Language,Const.Cache_Control,Const.Connection,Const.Host,Const.Origin,Const.Upgrade_Insecure_Request);
    private static  Map<String, String> data = AdminInfo.makeLoginData();


    public static Response getCookieValueConnection() throws IOException {
        return connect(Const.CookieValueUrl, Method.POST, commonSetting, Const.CookieValue_Referer, Const.Content_Length, Const.Content_Type, data);
    }
    public static Response login(String cookie_value) throws IOException {
        return connect(Const.LoginUrl, Method.GET, commonSetting, Const.Login_Referer, cookie_value, Const.Content_Length, Const.Content_Type, data);
    }
    public static Response getList(String cookie_value) throws IOException {
        return connect(Const.ListUrl, Method.GET,commonSetting, Const.List_Referer, cookie_value);
    }

    public static Response connect(String url, Method method, CommonSetting commonSetting, String referer, String content_length, String content_type, Map<String, String> data) throws IOException {
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
                .data(data)
                .execute();
    }

    public static Response connect(String url, Method method, CommonSetting commonSetting, String referer, String cookie_value, String content_length, String content_type, Map<String, String> data) throws IOException {
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
                .data(data)
                .execute();
    }
    public static Response connect(String url, Method method, CommonSetting commonSetting, String referer, String cookie_value) throws IOException {
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
                .data(data)
                .execute();
    }
}
