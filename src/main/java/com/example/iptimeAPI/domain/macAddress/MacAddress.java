package com.example.iptimeAPI.domain.macAddress;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

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


    public boolean isMacAddress(String compareMacAddress) {
        return macAddress.equals(compareMacAddress);
    }

}
