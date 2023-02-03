package com.example.iptimeAPI.domain.iptime;

import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

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

    public boolean isSameMacAddressList(List<String> latestMacAddressesList) {
        return this.macAddressesList.equals(latestMacAddressesList);
    }

    public boolean contain(String macAddress) {
        return this.macAddressesList.contains(macAddress);
    }

}
