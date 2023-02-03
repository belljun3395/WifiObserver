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
     * 연결된 iptime 설정페이지의 ip 정보와 ip 정보가 동일한지 판단하는 메서드입니다.
     * @param ipInfoRequest ip 정보입니다.
     * @return 요청한 ip 정보가 iptime 설정페이지와 동일하다면 true
     */
    boolean isInIptime(IpInfoRequest ipInfoRequest);

    /**
     * iptime 설정페이지에서 조회한 MAC 주소 리스트를 MAC 주소 기반으로 존재 여부를 확인하는 메서드입니다.
     * @param macAddress MAC 주소입니다.
     * @throws IOException
     */
    void isExistMacAddress(String macAddress) throws IOException;

    /**
     * 현재 동방에 존재하는 맴버를 등록된 MAC 주소 리스트를 기반으로 조회하는 메서드입니다.
     * @param registeredMacAddresses 서비스에 등록된 MAC 주소 리스트입니다.
     * @return 현재 동방에 존재하는 맴버을 반환합니다.
     */
    List<Long> browseExistMembers(List<MacAddressDTO> registeredMacAddresses);

    /**
     * iptime 설정페이지에서 MAC 주소 리스트를 조회하여 캐싱된 정보를 갱신하는 메서드입니다.
     * @throws IOException
     */
    void renewalList() throws IOException;

}
