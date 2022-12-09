package com.example.iptimeAPI.service.macAddress;

import com.example.iptimeAPI.controller.dto.MacAddressDTO;
import com.example.iptimeAPI.domain.macAddress.MacAddress;

import java.io.IOException;
import java.util.List;

public interface MacAddressService {

    void registerMacAddress(MacAddressDTO macAddressDTO);

    MacAddress validateRegistedMember(Long memberId);

    void checkMemberMacAddressIsExist(MacAddress macAddress) throws IOException;

    List<MacAddress> browseMacAddresses();

    List<MacAddressDTO> browseExistMember() throws IOException;

}

