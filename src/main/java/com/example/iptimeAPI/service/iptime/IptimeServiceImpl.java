package com.example.iptimeAPI.service.iptime;

import com.example.iptimeAPI.domain.iptime.Iptime;
import com.example.iptimeAPI.domain.iptime.IptimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class IptimeServiceImpl implements IptimeService {

    private final Iptime iptime;
    private String cookieValue;

    private List<String> macAddressesList;

    @Autowired
    public IptimeServiceImpl(Iptime iptime) throws IOException {
        this.iptime = iptime;
        this.cookieValue = iptime.getCookieValue();
        this.macAddressesList = iptime.getList(cookieValue);
    }

    @Override
    public List<String> getLatestMacAddressesList() throws IOException {
        return this.macAddressesList;
    }

    @Scheduled(fixedDelay = 3000)
    public void renewalList() throws IOException {
        List<String> latestMacAddressesList = this.getMacAddressesList();
        if (!macAddressesList.equals(latestMacAddressesList)) {
            this.macAddressesList = latestMacAddressesList;
        }
    }

    public List<String> getMacAddressesList() throws IOException {
        List<String> list = iptime.getList(cookieValue);
        if (!list.isEmpty()) {
            return list;
        }
        this.cookieValue = iptime.getCookieValue();
        return iptime.getList(cookieValue);
    }
}
