package com.example.iptimeAPI.web.controller.macAddress;

import com.example.iptimeAPI.web.dto.MacAddressDTO;
import com.example.iptimeAPI.domain.macAddress.MacAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/macs")
@RequiredArgsConstructor
public class MacAddressController {

    private final MacAddressService macAddressService;

    @PostMapping
    public void registerMac(MacAddressDTO macAddressDTO) {
        macAddressService.registerMacAddress(macAddressDTO);
    }


}
