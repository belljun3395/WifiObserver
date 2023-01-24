package com.example.iptimeAPI.domain.macAddress;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MacAddressRepository extends JpaRepository<MacAddress, Long> {

    Optional<MacAddress> findByMemberId(Long memberId);

    Optional<MacAddress> findByMacAddress(String macAddress);

}
