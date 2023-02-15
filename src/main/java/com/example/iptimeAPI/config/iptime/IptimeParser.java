package com.example.iptimeAPI.config.iptime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 * iptime 설정페이지에 요청해서 받아온 응답에서 원하는 값을 얻기 위해 파싱하기 위한 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class IptimeParser {
    private final Pattern findSetCookie = Pattern.compile("setCookie\\(\'[^\\(\\)]+\'\\)");
    private final Pattern extractCookieName = Pattern.compile("([^\\(\\)]+)");
    private final String VOID = "";


    private final IptimeHTMLConfig iptimeHTMLConfig;


    /**
     * @param connectResponse iptime 설정페이지에 쿠키를 얻기 위해 요청의 응답
     * @return 쿠기 값
     * @throws IOException
     */
    public String parseCookieValueQueryResponse(Response connectResponse) throws IOException {

        Document loginPageDocument = connectResponse.parse();
        String bodyString = loginPageDocument.body().toString();
        String findCookie = findBracketTextByPattern(findSetCookie, bodyString);
        return findBracketTextByPattern(extractCookieName, findCookie).replaceAll("\'", "");
    }

    /**
     * @param connectResponse iptime 설정페이지에 MAC 주소 리스트를 얻기 위해 요청의 응답
     * @return MAC 주소 리스트
     * @throws IOException
     */
    public List<String> parseMacAddressListQueryResponse(Response connectResponse)
        throws IOException {

        Element body = connectResponse.parse().body();

        Elements tbody = body.select(iptimeHTMLConfig.getTbody());

        List<Element> td = getTd(tbody);

        List<Element> input = getInputBefore(tbody);

        List<String> tdValue = getTdValue(td);

        List<String> inputValue = getInputValue(input);

        return getResult_Only_MAC(tdValue, inputValue);
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
