package com.example.iptimeAPI.domain.macAddress;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MacAddressRepository extends JpaRepository<MacAddress, Long> {

    /**
     * memberId를 기준으로 MAC 주소 등록 정보를 조회하는 메서드입니다.
     * @param memberId member의 id 입니다.
     * @return memberId를 기준으로 조회한 MAC 주소 등록 정보입니다.
     */
    Optional<MacAddress> findByMemberId(Long memberId);

    /**
     * MAC 주소를 기준으로 MAC 주소 등록 정보를 조회하는 메서드입니다.
     * @param macAddress MAC 주소입니다.
     * @return MAC 주소를 기준으로 MAC 주소 등록 정보입니다.
     */
    Optional<MacAddress> findByMacAddress(String macAddress);

}
