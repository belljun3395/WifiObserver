package com.example.iptimeAPI.service.macAddress;

import com.example.iptimeAPI.mapper.macAddress.MacAddressMapper;
import com.example.iptimeAPI.mapper.macAddress.MacAddressDTO;
import com.example.iptimeAPI.domain.macAddress.MacAddress;
import com.example.iptimeAPI.domain.macAddress.MacAddressRepository;
import com.example.iptimeAPI.domain.macAddress.MacAddressService;
import com.example.iptimeAPI.service.macAddress.exception.MacAddressValidateError;
import com.example.iptimeAPI.service.macAddress.exception.MacAddressValidateException;
import com.example.iptimeAPI.web.dto.MacAddressEditRequest;
import com.example.iptimeAPI.web.dto.MacAddressRegistRequest;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MacAddressServiceImpl implements MacAddressService {

    private final MacAddressRepository repository;

    private final MacAddressMapper converter;


    @Override
    @Transactional
    public void registerMacAddress(MacAddressRegistRequest macAddressRegistDTO) {
        Optional<MacAddress> byMemberId =
            repository.findByMemberId(macAddressRegistDTO.getMemberId());

        Optional<MacAddress> byMacAddress =
            repository.findByMacAddress(macAddressRegistDTO.getMacAddress());

        validateMacAddress(
            byMacAddress,
            Optional::isPresent,
            MacAddressValidateError.DUPLICATE_MACADDRESS
        );

        if (byMemberId.isPresent()) {
            validateMacAddress(
                byMemberId,
                mac
                    -> mac.get().isMacAddress(macAddressRegistDTO.getMacAddress()),
                MacAddressValidateError.DUPLICATE_MACADDRESS
            );

            MacAddress macAddress = byMemberId.get();

            repository.save(
                new MacAddress(
                    macAddress.getId(),
                    macAddressRegistDTO.getMemberId(),
                    macAddressRegistDTO.getMacAddress()
                )
            );
        }

        if (byMemberId.isEmpty()) {
            repository.save(
                new MacAddress(
                    macAddressRegistDTO.getMemberId(),
                    macAddressRegistDTO.getMacAddress()
                )
            );
        }
    }

    @Override
    @Transactional
    public void editMacAddress(MacAddressEditRequest macAddressEditDTO) {
        // todo 검증 로직
        repository.save(
            new MacAddress(
                macAddressEditDTO.getId(),
                macAddressEditDTO.getMemberId(),
                macAddressEditDTO.getMacAddress()
            )
        );
    }

    @Override
    public List<MacAddressDTO> browseMacAddresses() {
        return repository.findAll()
            .stream()
            .map(converter::from)
            .collect(Collectors.toList());
    }

    @Override
    public List<Long> browseMacAddressesMembers() {
        return repository.findAll()
            .stream()
            .map(MacAddress::getMemberId)
            .collect(Collectors.toList());
    }

    @Override
    public MacAddressDTO findMemberMacAddress(Long memberId) {
        Optional<MacAddress> byMemberId = repository.findByMemberId(memberId);

        validateMacAddress(
            byMemberId,
            Optional::isEmpty,
            MacAddressValidateError.NOT_REGISTER_MEMBER
        );

        MacAddress macAddress = byMemberId.get();

        return converter.from(macAddress);
    }

    private void validateMacAddress(Optional<MacAddress> macAddress,
        Predicate<Optional<MacAddress>> predicate, MacAddressValidateError error) {

        if (predicate.test(macAddress)) {
            throw new MacAddressValidateException(error);
        }
    }

}
