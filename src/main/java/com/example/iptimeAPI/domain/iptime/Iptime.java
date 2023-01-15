package com.example.iptimeAPI.domain.iptime;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class Iptime {

    private final Pattern findSetCookie = Pattern.compile("setCookie\\(\'[^\\(\\)]+\'\\)");
    private final Pattern extractCookieName = Pattern.compile("([^\\(\\)]+)");
    private final String VOID = "";

    private final IptimeHTML iptimeHTML;
    private final IptimeConnection iptimeConnection;

    public String getIp() {
        return iptimeConnection.getIp();
    }

    public String getCookieValue() throws IOException {
        Response cookieValueResponse = iptimeConnection.getCookieValue();

        Document loginPageDocument = cookieValueResponse.parse();
        String bodyString = loginPageDocument.body()
                .toString();
        String findCookie = findBracketTextByPattern(findSetCookie, bodyString);
        String cookieValue = findBracketTextByPattern(extractCookieName, findCookie).replaceAll("\'", "");

        return cookieValue;
    }


    public List<String> getList(String cookieValue) throws IOException {
        Response listResponsePage = iptimeConnection.getList(cookieValue);

        Element body = listResponsePage.parse()
                .body();
        Elements tbody = body.select(iptimeHTML.getTbody());

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
                    .select(iptimeHTML.getTr());
            Elements td = tr.select(iptimeHTML.getTd());
            for (Element j : td) {
                if (!j.toString()
                        .contains(iptimeHTML.getStyle())) {
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
                    .select(iptimeHTML.getInput());
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

