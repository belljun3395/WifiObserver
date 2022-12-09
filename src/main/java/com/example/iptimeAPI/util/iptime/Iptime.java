package com.example.iptimeAPI.util.iptime;

import com.example.iptimeAPI.util.iptime.info.ConnectionInfo;
import com.example.iptimeAPI.util.iptime.config.Const;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Iptime {

    private static final Pattern findSetCookie = Pattern.compile("setCookie\\(\'[^\\(\\)]+\'\\)");
    private static final Pattern extractCookieName = Pattern.compile("([^\\(\\)]+)");
    private static final String VOID = "";


    public String getCookieValue() throws IOException {
        Response cookieValueResponse = ConnectionInfo.getCookieValueConnection();

        Document loginPageDocument = cookieValueResponse.parse();
        String bodyString = loginPageDocument.body()
                .toString();
        String findCookie = findBracketTextByPattern(findSetCookie, bodyString);
        String cookieValue = findBracketTextByPattern(extractCookieName, findCookie).replaceAll("\'", "");

        return cookieValue;
    }

    public void login(String cookie_value) throws IOException {
        ConnectionInfo.login(cookie_value);
    }


    public List<String> getList(String cookieValue) throws IOException {
        Response listResponsePage = ConnectionInfo.getList(cookieValue);

        Element body = listResponsePage.parse()
                .body();
        Elements tbody = body.select(Const.Tag_Tbody);

        List<Element> td = getTd(tbody);

        List<Element> input = getInputBefore(tbody);

        List<String> tdValue = getTdValue(td);

        List<String> inputValue = getInputValue(input);

//        List<String> result = getResult_ALL(tdValue, inputValue);

        List<String> result = getResult_Only_MAC(tdValue, inputValue);

//        for (String r : result) {
//            System.out.println(r);
//        }

        return result;
    }

    private List<Element> getTd(Elements tbody) {
        List<Element> tdElement = new ArrayList<>();
        for (int i = 0; i < tbody.size(); i++) {
            Elements tr = tbody.get(i)
                    .select(Const.Tag_Tr);
            Elements td = tr.select(Const.Tag_Td);
            for (Element j : td) {
                if (!j.toString()
                        .contains(Const.Extract_Style_Tag)) {
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
                    .select(Const.Tag_Input);
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
                result.add(inputValue.get(i).toUpperCase());
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

    /**
     * 텍스트에 존재하는 괄호 내용 모두 추출 <p>
     * @param text
     * @return
     */
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

