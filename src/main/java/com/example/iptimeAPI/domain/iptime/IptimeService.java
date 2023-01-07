package com.example.iptimeAPI.domain.iptime;

import com.example.iptimeAPI.domain.macAddress.MacAddress;
import com.example.iptimeAPI.web.dto.IpDTO;

import java.util.List;

public interface IptimeService {

    boolean isInIptime(IpDTO ipDTO);

    boolean isExistMacAddress(MacAddress macAddress);

    List<Long> browseExistMembers(List<MacAddress> registeredMacAddresses );
}
