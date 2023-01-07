package com.example.iptimeAPI.domain.macAddress;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public boolean isSameMacAddress(String macAddress) {
        return this.macAddress.equals(macAddress);
    }

    @Data
    public static class MacAddressResponseDTO {

        private Long id;

        private Long memberId;

        private String macAddress;

        public MacAddressResponseDTO(Long id, Long memberId, String macAddress) {
            this.id = id;
            this.memberId = memberId;
            this.macAddress = macAddress;
        }
    }

}
