package com.example.iptimeAPI.domain.macAddress;

import com.example.iptimeAPI.web.dto.MacAddressEditDTO;
import com.example.iptimeAPI.web.dto.MacAddressRegistDTO;

import java.util.List;

public interface MacAddressService {

    void registerMacAddress(MacAddressRegistDTO macAddressRegistDTO);

    void editMacAddress(MacAddressEditDTO macAddressEditDTO);

    List<MacAddress.MacAddressResponseDTO> browseMacAddresses();

    List<Long> browseMacAddressesMembers();

    MacAddress.MacAddressResponseDTO findMemberMacAddress(Long memberId);

}

