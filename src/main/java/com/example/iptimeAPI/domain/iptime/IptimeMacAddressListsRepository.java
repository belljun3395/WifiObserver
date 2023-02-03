package com.example.iptimeAPI.domain.iptime;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface IptimeMacAddressListsRepository extends CrudRepository<IptimeMacAddressLists, String> {

    /**
     * ip를 기준으로 캐싱된 MAC 주소 리스트를 조회하는 메서드입니다.
     * @param ip 조회할 기준 ip
     * @return 캐싱된 MAC 주소 리스트
     */
    Optional<IptimeMacAddressLists> findByIp(String ip);

}
