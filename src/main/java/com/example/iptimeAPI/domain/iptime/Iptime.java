package com.example.iptimeAPI.domain.iptime;

import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class Iptime {

    private final IptimeConnection iptimeConnection;


    public Iptime(IptimeConnection iptimeConnection) {
        this.iptimeConnection = iptimeConnection;
    }


    public boolean isIn(String ip) {
        return iptimeConnection.isConnect(ip);
    }

    public String getValueOfIp() {
        return iptimeConnection.getValueOfIp();
    }

    // todo cache
    public String queryCookieValue() throws IOException {
        return iptimeConnection.queryCookieValue();
    }

    public List<String> queryMacAddressList(String cookieValue) throws IOException {
        return iptimeConnection.queryMacAddressList(cookieValue);
    }

}

