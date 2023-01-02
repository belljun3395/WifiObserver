package com.example.iptimeAPI.util.iptime.service;

import com.example.iptimeAPI.util.iptime.Iptime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class IptimeServiceImpl implements IptimeService {

    private final Iptime iptime;
    private String cookieValue;

    @Autowired
    public IptimeServiceImpl(Iptime iptime) throws IOException {
        this.iptime = iptime;
        this.cookieValue = iptime.getCookieValue();
    }

    @Override
    public List<String> getMacAddressesList() throws IOException {
        List<String> list = iptime.getList(cookieValue);
        if (!list.isEmpty()) {
            return list;
        }
        this.cookieValue = iptime.getCookieValue();
        return iptime.getList(cookieValue);
    }
}
