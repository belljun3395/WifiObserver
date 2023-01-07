package com.example.iptimeAPI.service.macAddress;

import com.example.iptimeAPI.web.dto.MacAddressRegistDTO;
import com.example.iptimeAPI.domain.macAddress.MacAddress;
import com.example.iptimeAPI.domain.macAddress.MacAddressRepository;
import com.example.iptimeAPI.domain.macAddress.MacAddressService;
import com.example.iptimeAPI.web.dto.MacAddressResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MacAddressServiceImpl implements MacAddressService {

    private final MacAddressRepository repository;

    @Override
    public void registerMacAddress(MacAddressRegistDTO macAddressRegistDTO) {
        repository.save(macAddressRegistDTO.convertToMacAddress());
    }

    @Override
    public void editMacAddress(MacAddressResponseDTO macAddressResponseDTO) {
        repository.save(new MacAddress(macAddressResponseDTO.getId(), macAddressResponseDTO.getMemberId(), macAddressResponseDTO.getMacAddress()));
    }

    @Override
    public List<MacAddress> browseMacAddresses() {
        return repository.findAll();
    }

    @Override
    public List<Long> browseMacAddressesMembers() {
        List<MacAddress> all = repository.findAll();
        return all.stream()
                .map(macAddress -> macAddress.getMemberId())
                .collect(Collectors.toList());
    }

    @Override
    public MacAddress findMemberMacAddress(Long memberId) {
        Optional<MacAddress> byMemberId = repository.findByMemberId(memberId);
        if (byMemberId.isEmpty()) {
            throw new IllegalStateException("this member need to register to DB");
        }
        return byMemberId.get();
    }
}
