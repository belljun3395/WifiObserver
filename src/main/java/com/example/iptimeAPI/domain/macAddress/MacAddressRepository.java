package com.example.iptimeAPI.domain.macAddress;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MacAddressRepository extends JpaRepository<MacAddress, Long> {

    Optional<MacAddress> findByMemberId(Long memberId);

    Optional<MacAddress> findByMacAddress(String macAddress);

}
