package com.example.iptimeAPI.mapper.macAddress;


import com.example.iptimeAPI.domain.macAddress.MacAddress;
import org.springframework.stereotype.Component;

@Component
public class MacAddressMapper {
    public MacAddressDTO from(MacAddress macAddress) {
        return
            new MacAddressDTO(
                macAddress.getId(),
                macAddress.getMemberId(),
                macAddress.getMacAddress()
            );
    }
}
