package com.example.iptimeAPI.domain.iptime;

import com.example.iptimeAPI.config.iptime.IptimeParser;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection.Response;
import org.springframework.stereotype.Component;

/**
 * iptime 설정페이지의 값을 조회할 수 있는 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class Iptime {
    private final IptimeConnection iptimeConnection;

    private final IptimeParser iptimeParser;


    /**
     * @param ip ip
     * @return ip가 연결된 iptime 설정페이지 ip와 일치하면 true
     */
    public boolean isIn(String ip) {
        return iptimeConnection.isConnect(ip);
    }

    /**
     * @return 연결된 iptime 설정페이지의 ip 입니다.
     */
    public String getValueOfIp() {
        return iptimeConnection.getValueOfIp();
    }

    /**
     * @return 연결된 iptime 설정페이지의 쿠키 값입니다.
     * @throws IOException
     */
    public String queryCookieValue() throws IOException {
        Response response = iptimeConnection.queryCookieValue();
        return iptimeParser.parseCookieValueQueryResponse(response);
    }

    /**
     * @param cookieValue 쿠키 값
     * @return iptime 설정페이지에서 MAC 주소 리스트입니다.
     * @throws IOException
     */
    public List<String> queryMacAddressList(String cookieValue) throws IOException {
        Response response = iptimeConnection.queryMacAddressList(cookieValue);

        return iptimeParser.parseMacAddressListQueryResponse(response);
    }
}

