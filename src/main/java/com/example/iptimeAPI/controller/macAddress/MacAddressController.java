package com.example.iptimeAPI.controller.macAddress;

import com.example.iptimeAPI.controller.dto.MacAddressDTO;
import com.example.iptimeAPI.service.macAddress.MacAddressService;
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
