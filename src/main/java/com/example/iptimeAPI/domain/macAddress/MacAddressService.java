package com.example.iptimeAPI.domain.macAddress;

import com.example.iptimeAPI.mapper.macAddress.MacAddressDTO;
import com.example.iptimeAPI.web.dto.MacAddressEditRequest;
import com.example.iptimeAPI.web.dto.MacAddressRegistRequest;
import java.util.List;

public interface MacAddressService {

    /**
     * MAC 주소 정보를 등록하기 위한 메서드입니다.
     * @param macAddressRegistRequest MAC 주소 정보를 등록하기 위한 정보
     */
    void registerMacAddress(MacAddressRegistRequest macAddressRegistRequest);

    /**
     * MAC 주소 정보를 수정하기 위한 메서드입니다.
     * @param macAddressEditRequest MAC 주소 정보를 수정하기 위한 정보
     */
    void editMacAddress(MacAddressEditRequest macAddressEditRequest);

    /**
     * @return 등록된 MAC 주소 정보를 반환합니다.
     */
    List<MacAddressDTO> browseMacAddresses();

    /**
     * @return MAC 주소를 등록한 전체 member의 id를 반환합니다.
     */
    List<Long> browseMacAddressesMembers();

    /**
     * @param memberId member의 id
     * @return member의 id를 기준으로 조회한 등록된 MAC 주소 정보를 반환합니다.
     */
    MacAddressDTO findMemberMacAddress(Long memberId);

}

