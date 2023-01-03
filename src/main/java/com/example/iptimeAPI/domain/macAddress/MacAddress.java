package com.example.iptimeAPI.domain.macAddress;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    public boolean checkExist(List<String> macAddresses) {
        return macAddresses.contains(this.macAddress);
    }
}
