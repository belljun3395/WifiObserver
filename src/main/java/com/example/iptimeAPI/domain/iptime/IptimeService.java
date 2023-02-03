package com.example.iptimeAPI.domain.iptime;

import com.example.iptimeAPI.mapper.macAddress.MacAddressDTO;
import com.example.iptimeAPI.web.dto.IpInfoRequest;
import java.io.IOException;
import java.util.List;

public interface IptimeService {

    boolean isInIptime(IpInfoRequest ipDTO);

    void isExistMacAddress(String macAddress) throws IOException;

    List<Long> browseExistMembers(List<MacAddressDTO> registeredMacAddresses);

    void renewalList() throws IOException;

}
