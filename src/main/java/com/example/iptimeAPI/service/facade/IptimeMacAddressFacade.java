package com.example.iptimeAPI.service.facade;

import com.example.iptimeAPI.domain.iptime.IptimeService;
import com.example.iptimeAPI.domain.macAddress.MacAddress.MacAddressResponseDTO;
import com.example.iptimeAPI.domain.macAddress.MacAddressService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IptimeMacAddressFacade {

    private final IptimeService iptimeService;
    private final MacAddressService macAddressService;

    public List<Long> browseExistMembers() {
        List<MacAddressResponseDTO> macAddresses = macAddressService.browseMacAddresses();
        return iptimeService.browseExistMembers(macAddresses);
    }

    public void validateExistMemberMacAddress(Long memberId) throws IOException {
        String macAddress = macAddressService.findMemberMacAddress(memberId)
            .getMacAddress();
        iptimeService.isExistMacAddress(macAddress);
    }

}
