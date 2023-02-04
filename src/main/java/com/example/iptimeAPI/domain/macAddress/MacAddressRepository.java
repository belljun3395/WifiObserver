package com.example.iptimeAPI.domain.macAddress;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MacAddressRepository extends JpaRepository<MacAddress, Long> {

    /**
     * @param memberId member의 id
     * @return memberId를 기준으로 조회한 MAC 주소 등록 정보를 반환합니다.
     */
    Optional<MacAddress> findByMemberId(Long memberId);

    /**
     * @param macAddress MAC 주소
     * @return MAC 주소를 기준으로 조회한 MAC 주소 등록 정보를 반환합니다.
     */
    Optional<MacAddress> findByMacAddress(String macAddress);

}
