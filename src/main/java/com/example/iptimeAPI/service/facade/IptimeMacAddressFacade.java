package com.example.iptimeAPI.service.facade;

import com.example.iptimeAPI.mapper.macAddress.MacAddressDTO;
import com.example.iptimeAPI.domain.iptime.IptimeService;
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


    /**
     * @return 현재 동방에 존재하는 member를 반환합니다.
     */
    public List<Long> browseExistMembers() {
        List<MacAddressDTO> macAddresses = macAddressService.browseMacAddresses();

        return iptimeService.browseExistMembers(macAddresses);
    }

    /**
     * 현재 동방에 존재하는 member인지 MAC 주소를 기반으로 검증하는 기능입니다.
     * @param memberId memeber의 id
     * @throws IOException
     */
    public void validateExistMemberMacAddress(Long memberId) throws IOException {
        String macAddress =
            macAddressService
                .findMemberMacAddress(memberId)
                .getMacAddress();

        iptimeService.isExistMacAddress(macAddress);
    }

}
