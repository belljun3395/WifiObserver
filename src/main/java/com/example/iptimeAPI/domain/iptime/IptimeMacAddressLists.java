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
     * @param latestMacAddressesList MAC 주소 리스트
     * @return 가지고있는 MAC 주소 리스트와 MAC 주소 리스트가 동일하면 true
     */
    public boolean isSameMacAddressList(List<String> latestMacAddressesList) {
        if (macAddressesList == null) {
            return false;
        }
        return macAddressesList.equals(latestMacAddressesList);
    }

    /**
     * @param macAddress MAC 주소
     * @return 가지고있는 MAC 주소 리스트에 MAC 주소가 포함되어 있다면 true
     */
    public boolean contain(String macAddress) {
        return macAddressesList.contains(macAddress);
    }
}
