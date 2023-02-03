package com.example.iptimeAPI.domain.macAddress;

import com.example.iptimeAPI.mapper.macAddress.MacAddressDTO;
import com.example.iptimeAPI.web.dto.MacAddressEditRequest;
import com.example.iptimeAPI.web.dto.MacAddressRegistRequest;
import java.util.List;

public interface MacAddressService {

    void registerMacAddress(MacAddressRegistRequest macAddressRegistDTO);

    void editMacAddress(MacAddressEditRequest macAddressEditDTO);

    List<MacAddressDTO> browseMacAddresses();

    List<Long> browseMacAddressesMembers();

    MacAddressDTO findMemberMacAddress(Long memberId);

}

