package com.example.iptimeAPI.domain.iptime;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface IptimeMacAddressListsRepository extends CrudRepository<IptimeMacAddressLists, String> {

    Optional<IptimeMacAddressLists> findByIp(String ip);

}
