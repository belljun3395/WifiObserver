package com.example.iptimeAPI.domain.macAddress;

import com.example.iptimeAPI.web.dto.MacAddressDTO;

import java.io.IOException;
import java.util.List;

public interface MacAddressService {

    void registerMacAddress(MacAddressDTO macAddressDTO);

    List<Long> browseRegistedMembers();

    MacAddress validateRegistedMember(Long memberId);

    void checkMemberMacAddressIsExist(MacAddress macAddress) throws IOException;

    List<MacAddress> browseMacAddresses();

    List<MacAddressDTO> browseExistMember() throws IOException;

}

