package com.example.iptimeAPI.domain.iptime;

import com.example.iptimeAPI.mapper.macAddress.MacAddressDTO;
import com.example.iptimeAPI.web.dto.IpInfoRequest;
import java.io.IOException;
import java.util.List;

/**
 * iptime과 관련된 서비스를 위한 서비스입니다.
 */
public interface IptimeService {

    /**
     * @param ipInfoRequest ip
     * @return ip가 iptime 설정페이지의 ip와 동일하다면 true
     */
    boolean isInIptime(IpInfoRequest ipInfoRequest);

    /**
     * MAC 주소 리스트에 MAC 주소가 존재하는지 확인하는 메서드입니다.
     * @param macAddress MAC 주소
     * @throws IOException
     */
    void isExistMacAddress(String macAddress) throws IOException;

    /**
     * @param registeredMacAddresses MAC 주소 리스트
     * @return 현재 동방에 존재하는 member를 반환합니다.
     */
    List<Long> browseExistMembers(List<MacAddressDTO> registeredMacAddresses);

    /**
     * iptime 설정페이지에서 MAC 주소 리스트를 조회하여 캐싱된 정보를 갱신하는 메서드입니다.
     * @throws IOException
     */
    void renewalList() throws IOException;

}
