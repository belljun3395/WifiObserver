package com.example.iptimeAPI.service.macAddress;

import com.example.iptimeAPI.web.dto.MacAddressEditDTO;
import com.example.iptimeAPI.web.dto.MacAddressRegistDTO;
import com.example.iptimeAPI.domain.macAddress.MacAddress;
import com.example.iptimeAPI.domain.macAddress.MacAddressRepository;
import com.example.iptimeAPI.domain.macAddress.MacAddressService;
import com.example.iptimeAPI.service.macAddress.exception.MacAddressValidateError;
import com.example.iptimeAPI.service.macAddress.exception.MacAddressValidateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MacAddressServiceImpl implements MacAddressService {

    private final MacAddressRepository repository;

    @Override
    @Transactional
    public void registerMacAddress(MacAddressRegistDTO macAddressRegistDTO) {
        Optional<MacAddress> byMemberId = repository.findByMemberId(macAddressRegistDTO.getMemberId());
        Optional<MacAddress> byMacAddress = repository.findByMacAddress(macAddressRegistDTO.getMacAddress());

        validateMacAddress(byMacAddress, Optional::isPresent, MacAddressValidateError.DUPLICATE_MACADDRESS);

        if (byMemberId.isPresent()) {
            validateMacAddress(byMemberId,
                    mac -> mac.get()
                            .isSameMacAddress(macAddressRegistDTO.getMacAddress()),
                    MacAddressValidateError.DUPLICATE_MACADDRESS);
            MacAddress macAddress = byMemberId.get();
            // todo 조금 더 고민해 보기
//            this.editMacAddress(new MacAddressEditDTO(macAddress.getId(), macAddressRegistDTO.getMemberId(), macAddressRegistDTO.getMacAddress()));
        }
        if (byMemberId.isEmpty()) {
            repository.save(new MacAddress(macAddressRegistDTO.getMemberId(), macAddressRegistDTO.getMacAddress()));
        }
    }

    @Override
    @Transactional
    public void editMacAddress(MacAddressEditDTO macAddressEditDTO) {
        // todo study update를 어떠한 방식으로 하는 것이 좋을까? DTO를 통해 들어오는 정보중 어느 것이 수정된 것인지 알 수 없기에 아래와 같이 새로운 객체를 반환하는 것으로 구현하였다.
        repository.save(new MacAddress(macAddressEditDTO.getId(), macAddressEditDTO.getMemberId(), macAddressEditDTO.getMacAddress()));
    }

    @Override
    public List<MacAddress.MacAddressResponseDTO> browseMacAddresses() {
        return repository.findAll()
                .stream()
                .map(macAddress -> new MacAddress.MacAddressResponseDTO(macAddress.getId(), macAddress.getMemberId(), macAddress.getMacAddress()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> browseMacAddressesMembers() {
        return repository.findAll().stream()
                .map(MacAddress::getMemberId)
                .collect(Collectors.toList());
    }

    @Override
    public MacAddress.MacAddressResponseDTO findMemberMacAddress(Long memberId) {
        Optional<MacAddress> byMemberId = repository.findByMemberId(memberId);
        validateMacAddress(byMemberId, Optional::isEmpty, MacAddressValidateError.NOT_REGISTER_MEMBER);
        MacAddress macAddress = byMemberId.get();
        return new MacAddress.MacAddressResponseDTO(macAddress.getId(), macAddress.getMemberId(), macAddress.getMacAddress());
    }

    private void validateMacAddress(Optional<MacAddress> macAddress, Predicate<Optional<MacAddress>> predicate, MacAddressValidateError error) {
        if (predicate.test(macAddress)) {
            throw new MacAddressValidateException(error);
        }
    }
}
