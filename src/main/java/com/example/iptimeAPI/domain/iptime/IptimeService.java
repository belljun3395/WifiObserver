package com.example.iptimeAPI.domain.iptime;

import com.example.iptimeAPI.domain.macAddress.MacAddress;
import com.example.iptimeAPI.web.dto.IpResponseDTO;
import com.example.iptimeAPI.web.dto.IpDTO;

import java.util.List;

public interface IptimeService {

    IpResponseDTO isInIptime(IpDTO ipDTO);

    boolean isExistMacAddress(String macAddress);

    List<Long> browseExistMembers(List<MacAddress.MacAddressResponseDTO> registeredMacAddresses );
}
