package com.example.iptimeAPI.domain.iptime;

import com.example.iptimeAPI.config.iptime.IptimeAdminConfig;
import com.example.iptimeAPI.config.iptime.IptimeHTMLConfig;
import com.example.iptimeAPI.config.iptime.IptimeHTTPConfig;
import com.example.iptimeAPI.config.iptime.info.IptimeInfoConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IptimeConnection {

    private final Pattern findSetCookie = Pattern.compile("setCookie\\(\'[^\\(\\)]+\'\\)");
    private final Pattern extractCookieName = Pattern.compile("([^\\(\\)]+)");
    private final String VOID = "";


    private final IptimeInfoConfig iptimeInfoConfig;
    private final IptimeHTTPConfig iptimeHTTPConfig;
    private final IptimeAdminConfig iptimeAdminConfig;
    private final IptimeHTMLConfig iptimeHTMLConfig;
    private final IptimeConnectionCommonConfig iptimeConnectionCommonConfig;


    public boolean isConnect(String ip) {
        return iptimeInfoConfig.isIp(ip);
    }

    public String getValueOfIp() {
        return iptimeInfoConfig.getIp();
    }

    public String queryCookieValue() throws IOException {
        Response connectResponse = connect(
            iptimeHTTPConfig.get_cookie_value(),
            Method.POST,
            iptimeConnectionCommonConfig,
            iptimeHTTPConfig.get_cookie_value_referer(),
            iptimeHTTPConfig.getContent_length(),
            iptimeHTTPConfig.getContent_type()
        );

        Document loginPageDocument = connectResponse.parse();
        String bodyString = loginPageDocument.body().toString();
        String findCookie = findBracketTextByPattern(findSetCookie, bodyString);
        String cookieValue = findBracketTextByPattern(extractCookieName, findCookie).replaceAll("\'", "");

        return cookieValue;
    }

    public List<String> queryMacAddressList(String cookie_value) throws IOException {
        Response connectResponse =  connect(
            iptimeHTTPConfig.get_list_url(),
            Method.GET,
            iptimeConnectionCommonConfig,
            iptimeHTTPConfig.get_list_referer(),
            cookie_value
        );

        Element body = connectResponse.parse().body();

        Elements tbody = body.select(iptimeHTMLConfig.getTbody());

        List<Element> td = getTd(tbody);

        List<Element> input = getInputBefore(tbody);

        List<String> tdValue = getTdValue(td);

        List<String> inputValue = getInputValue(input);

        return getResult_Only_MAC(tdValue, inputValue);
    }

    private Response connect(String url, Method method, IptimeConnectionCommonConfig commonSetting, String referer,
        String content_length, String content_type) throws IOException {
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

    private Response connect(String url, Method method, IptimeConnectionCommonConfig commonSetting, String referer,
        String cookie_value) throws IOException {
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


    private List<Element> getTd(Elements tbody) {
        List<Element> tdElement = new ArrayList<>();
        for (int i = 0; i < tbody.size(); i++) {
            Elements tr = tbody.get(i)
                .select(iptimeHTMLConfig.getTr());
            Elements td = tr.select(iptimeHTMLConfig.getTd());
            for (Element j : td) {
                if (!j.toString()
                    .contains(iptimeHTMLConfig.getStyle())) {
                    tdElement.add(j);
                }

            }
        }
        return tdElement;
    }

    private List<Element> getInputBefore(Elements tbody) {
        List<Element> inputElement = new ArrayList<>();
        for (int i = 0; i < tbody.size(); i++) {
            Elements input = tbody.get(i)
                .select(iptimeHTMLConfig.getInput());
            for (Element j : input) {
                inputElement.add(j);
            }
        }
        return inputElement;
    }

    private List<String> getTdValue(List<Element> tdBefore) {
        List<String> tdValue = new ArrayList<>();
        for (Element e : tdBefore) {
            tdValue.add(e.text()
                .replace("-", ":")
                .toLowerCase());
        }
        return tdValue;
    }

    private List<String> getInputValue(List<Element> inputBefore) {
        List<String> inputValue = new ArrayList<>();
        for (Element e : inputBefore) {
            inputValue.add(e.val()
                .toLowerCase());
        }
        return inputValue;
    }

    private List<String> getResult_Only_MAC(List<String> tdValue, List<String> inputValue) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < inputValue.size(); i = i + 3) {
            if (tdValue.contains(inputValue.get(i))) {
                result.add(inputValue.get(i)
                    .toUpperCase());
            }
        }
        return result;
    }

    private List<String> getResult_ALL(List<String> tdValue, List<String> inputValue) {
        List<String> result = new ArrayList<>();
        for (String s : inputValue) {
            if (tdValue.contains(s)) {
                result.add(s);
            }
        }
        return result;
    }

    private String findBracketTextByPattern(Pattern PATTERN, String text) {

        List<String> bracketTextList = new ArrayList<>();

        Matcher matcher = PATTERN.matcher(text);

        String pureText = text;
        String findText = new String();

        while (matcher.find()) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();

            findText = pureText.substring(startIndex, endIndex);
            pureText = pureText.replace(findText, VOID);
            matcher = PATTERN.matcher(pureText);

        }

        return findText;
    }

}
