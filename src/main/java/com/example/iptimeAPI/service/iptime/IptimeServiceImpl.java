package com.example.iptimeAPI.service.iptime;

import com.example.iptimeAPI.domain.iptime.Iptime;
import com.example.iptimeAPI.domain.iptime.IptimeService;
import com.example.iptimeAPI.domain.macAddress.MacAddress;
import com.example.iptimeAPI.web.dto.IpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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
    public boolean isInIptime(IpDTO ipDTO) {
        return iptime.getIp()
                .equals(ipDTO.getIp());
    }

    @Override
    public boolean isExistMacAddress(String macAddress) {
        return macAddressesList.contains(macAddress);
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

    @Override
    public List<Long> browseExistMembers(List<MacAddress.MacAddressResponseDTO> registeredMacAddresses) {
        List<Long> existMembers = new ArrayList<>();
        for (MacAddress.MacAddressResponseDTO mac : registeredMacAddresses) {
            if (this.isExistMacAddress(mac.getMacAddress())) {
                existMembers.add(mac.getMemberId());
            }
        }
        return existMembers;
    }
}
