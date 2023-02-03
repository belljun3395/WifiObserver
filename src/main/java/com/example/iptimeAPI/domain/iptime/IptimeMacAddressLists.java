package com.example.iptimeAPI.domain.iptime;

import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * iptime에서 조회한 MAC 주소 리스트를 캐싱하기 위한 엔티티입니다.
 */
@Getter
@RedisHash(value = "iptimeListMacAddressLists")
public class IptimeMacAddressLists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Indexed
    private String ip;

    private List<String> macAddressesList;


    public IptimeMacAddressLists(String ip, List<String> macAddressesList) {
        this.macAddressesList = macAddressesList;
        this.ip = ip;
    }

    /**
     * 캐싱된 MAC 주소 리스트와 iptime 설정페이지에서 조회한 MAC 주소 리스트가 동일한지 판단하는 메서드입니다.
     * @param latestMacAddressesList iptime 설정페이지에서 조회한 MAC 주소 리스트입니다.
     * @return 캐싱된 MAC 주소 리스트와 iptime 설정페이지에서 조회한 MAC 주소 리스트가 동일하면 true
     */
    public boolean isSameMacAddressList(List<String> latestMacAddressesList) {
        return this.macAddressesList.equals(latestMacAddressesList);
    }

    public boolean contain(String macAddress) {
        return this.macAddressesList.contains(macAddress);
    }

}
