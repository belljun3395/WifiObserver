package com.example.iptimeAPI.service.macAddress;

import com.example.iptimeAPI.web.dto.MacAddressDTO;
import com.example.iptimeAPI.domain.macAddress.MacAddress;
import com.example.iptimeAPI.domain.macAddress.MacAddressRepository;
import com.example.iptimeAPI.domain.macAddress.MacAddressService;
import com.example.iptimeAPI.util.iptime.service.IptimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MacAddressServiceImpl implements MacAddressService {

    private final IptimeService iptimeService;
    private final MacAddressRepository macAddressRepository;

    @Override
    public List<Long> browseRegisteredMembers() {
        List<MacAddress> all = macAddressRepository.findAll();
        return all.stream()
                .map(macAddress -> macAddress.getMemberId())
                .collect(Collectors.toList());
    }

    @Override
    public void registerMacAddress(MacAddressDTO macAddressDTO) {
        macAddressRepository.save(macAddressDTO.convertToMacAddress());
    }

    @Override
    public MacAddress validateRegisteredMember(Long memberId) {
        Optional<MacAddress> byMemberId = macAddressRepository.findByMemberId(memberId);
        if (byMemberId.isEmpty()) {
            throw new IllegalStateException("this member need to register to DB");
        }
        return byMemberId.get();
    }

    @Override
    public void checkMemberMacAddressIsExist(MacAddress macAddress) throws IOException {
        if (!macAddress.checkExist(iptimeService.getMacAddressesList())) {
            throw new IllegalStateException("this member need to re-register");
        }
    }

    @Override
    public List<MacAddress> browseMacAddresses() {
        return new ArrayList<>(macAddressRepository.findAll());
    }

    @Override
    public List<MacAddressDTO> browseExistMember() throws IOException {
        List<MacAddress> registeredMacAddresses = macAddressRepository.findAll();

        List<String> ipTimeMacAddressesList = iptimeService.getMacAddressesList();

        List<MacAddressDTO> macAddressDTOS = new ArrayList<>();
        for (MacAddress mac : registeredMacAddresses) {
            if (mac.checkExist(ipTimeMacAddressesList)) {
                macAddressDTOS.add(new MacAddressDTO(mac.getMemberId(), mac.getMacAddress()));
            }
        }
        return macAddressDTOS;
    }

}
