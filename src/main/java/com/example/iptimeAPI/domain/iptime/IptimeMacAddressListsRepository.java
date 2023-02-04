package com.example.iptimeAPI.domain.iptime;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface IptimeMacAddressListsRepository extends CrudRepository<IptimeMacAddressLists, String> {

    /**
     * @param ip ip
     * @return ip 기준으로 조회한 캐싱된 MAC 주소 리스트
     */
    Optional<IptimeMacAddressLists> findByIp(String ip);

}
