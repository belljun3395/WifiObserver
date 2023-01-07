package com.example.iptimeAPI.web.controller.macAddress;

import com.example.iptimeAPI.domain.macAddress.MacAddress;
import com.example.iptimeAPI.web.dto.MacAddressRegistDTO;
import com.example.iptimeAPI.domain.macAddress.MacAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/macs")
@RequiredArgsConstructor
public class MacAddressController {

    private final MacAddressService macAddressService;

    @PostMapping
    public void registerMacAddress(MacAddressRegistDTO macAddressRegistDTO) {
        macAddressService.registerMacAddress(macAddressRegistDTO);
    }

    @GetMapping("/{memberId}")
    public MacAddress.MacAddressResponseDTO findMemberMacAddress(@PathVariable("memberId") Long memberId) {
        MacAddress.MacAddressResponseDTO memberMacAddress = macAddressService.findMemberMacAddress(memberId);
        return new MacAddress.MacAddressResponseDTO(memberMacAddress.getId(), memberMacAddress.getMemberId(), memberMacAddress.getMacAddress());
    }

    @PutMapping("/{memberId}")
    public void editMemberMacAddress(MacAddress.MacAddressResponseDTO macAddressResponseDTO) {
        macAddressService.editMacAddress(macAddressResponseDTO);
    }

}
