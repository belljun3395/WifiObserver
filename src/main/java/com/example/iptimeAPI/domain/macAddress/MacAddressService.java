package com.example.iptimeAPI.domain.macAddress;

import com.example.iptimeAPI.web.dto.MacAddressRegistDTO;
import com.example.iptimeAPI.web.dto.MacAddressResponseDTO;

import java.util.List;

public interface MacAddressService {

    void registerMacAddress(MacAddressRegistDTO macAddressRegistDTO);

    void editMacAddress(MacAddressResponseDTO macAddressResponseDTO);

    List<MacAddress> browseMacAddresses();

    List<Long> browseMacAddressesMembers();

    MacAddress findMemberMacAddress(Long memberId);

}

