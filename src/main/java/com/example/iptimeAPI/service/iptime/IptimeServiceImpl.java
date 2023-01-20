package com.example.iptimeAPI.service.iptime;

import com.example.iptimeAPI.domain.iptime.Iptime;
import com.example.iptimeAPI.domain.iptime.IptimeService;
import com.example.iptimeAPI.domain.macAddress.MacAddress;
import com.example.iptimeAPI.service.iptime.dto.IpResponseDTO;
import com.example.iptimeAPI.service.exception.MacAddressValidateError;
import com.example.iptimeAPI.service.exception.MacAddressValidateException;
import com.example.iptimeAPI.web.dto.IpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
    public IpResponseDTO isInIptime(IpDTO ipDTO) {
        return new IpResponseDTO(iptime.isIn(ipDTO.getIp()));
    }

    @Override
    public void isExistMacAddress(String macAddress) throws IOException {
        try {
            isContain(macAddress);
        } catch (MacAddressValidateException macAddressValidateException) {
            this.renewalList();
            this.isContain(macAddress);
        }
    }

    private void isContain(String macAddress) {
        if (!macAddressesList.contains(macAddress)) {
            throw new MacAddressValidateException(MacAddressValidateError.NOT_EXIST_MACADDRESS);
        }
    }

    //    @Scheduled(fixedDelay = 3000)
    @Scheduled(fixedDelay = 60000 * 60)
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
        return registeredMacAddresses.stream()
                .filter(macAddressResponseDTO -> macAddressesList.contains(macAddressResponseDTO.getMacAddress()))
                .map(MacAddress.MacAddressResponseDTO::getMemberId)
                .collect(Collectors.toList());
    }

}
