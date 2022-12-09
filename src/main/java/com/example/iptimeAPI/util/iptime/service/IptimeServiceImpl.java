package com.example.iptimeAPI.util.iptime.service;

import com.example.iptimeAPI.util.iptime.Iptime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IptimeServiceImpl implements IptimeService {

    private final Iptime iptime;

    @Override
    public List<String> getMacAddressesList() throws IOException {
        String cookieValue = iptime.getCookieValue();
        iptime.login(cookieValue);
        return iptime.getList(cookieValue);
    }

}
