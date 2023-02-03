package com.example.iptimeAPI.domain.macAddress;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * MAC 주소를 관리하기 위한 엔티티입니다.
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "member_mac_address_list")
public class MacAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String macAddress;


    public MacAddress(Long memberId, String macAddress) {
        this.memberId = memberId;
        this.macAddress = macAddress;
    }

    public MacAddress(Long id, Long memberId, String macAddress) {
        this.id = id;
        this.memberId = memberId;
        this.macAddress = macAddress;
    }

    /**
     * 기존의 MAC 주소와 비교하는 MAC 주소가 일치하는지 확인하는 메서드입니다.
      * @param compareMacAddress 비교할 MAC 주소입니다.
     * @return 기존의 MAC 주소와 비교할 MAC 주소가 일치하면 true
     */
    public boolean isMacAddress(String compareMacAddress) {
        return macAddress.equals(compareMacAddress);
    }

}
