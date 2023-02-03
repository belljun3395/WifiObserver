package com.example.iptimeAPI.service.iptime;

import com.example.iptimeAPI.domain.iptime.Iptime;
import com.example.iptimeAPI.domain.iptime.IptimeConnection;
import com.example.iptimeAPI.mapper.macAddress.MacAddressDTO;
import com.example.iptimeAPI.domain.iptime.IptimeMacAddressLists;
import com.example.iptimeAPI.domain.iptime.IptimeMacAddressListsRepository;
import com.example.iptimeAPI.domain.iptime.IptimeService;
import com.example.iptimeAPI.service.macAddress.exception.MacAddressValidateError;
import com.example.iptimeAPI.service.macAddress.exception.MacAddressValidateException;
import com.example.iptimeAPI.web.dto.IpInfoRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class IptimeServiceImpl implements IptimeService {

    private final Iptime iptime;

    private final IptimeMacAddressListsRepository iptimeMacAddressListsRepository;


    @Autowired
    public IptimeServiceImpl(Iptime iptime, IptimeMacAddressListsRepository iptimeMacAddressListsRepository) {
        this.iptime = iptime;
        this.iptimeMacAddressListsRepository = iptimeMacAddressListsRepository;
    }

    @Override
    public boolean isInIptime(IpInfoRequest ipDTO) {
        return iptime.isIn(ipDTO.getIp());
    }

    @Override
    public void isExistMacAddress(String macAddress) throws IOException {
        try {
            isContain(macAddress);
        } catch (MacAddressValidateException macAddressValidateException) {
            renewalList();
            isContain(macAddress);
        }
    }

    //    @Scheduled(fixedDelay = 3000)
    @Scheduled(fixedDelay = 60000 * 60)
    public void renewalList() throws IOException {
        List<String> latestMacAddressesList = getIptimeMacAddressList();

        IptimeMacAddressLists currentIptimeMacAddressList = getCurrentIptimeMacAddressList();

        if (!currentIptimeMacAddressList.isSameMacAddressList(latestMacAddressesList)) {
            IptimeMacAddressLists iptimeMacAddressLists =
                new IptimeMacAddressLists(
                    iptime.getValueOfIp(),
                    latestMacAddressesList
                );

            iptimeMacAddressListsRepository.deleteAll();

            iptimeMacAddressListsRepository.save(iptimeMacAddressLists);
        }
    }

    /**
     * 연결된 ip 정보를 기반으로 현재 캐싱된 MAC 주소 리스트를 조회하는 메서드입니다.
     * @return 캐싱된 MAC 주소 리스트를 반환합니다.
     */
    private IptimeMacAddressLists getCurrentIptimeMacAddressList() {
        return iptimeMacAddressListsRepository.findByIp(iptime.getValueOfIp())
            .orElseGet(() -> {
                try {
                    return new IptimeMacAddressLists(iptime.getValueOfIp(), getIptimeMacAddressList());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * iptime 설정페이지에 MAC 주소 리스트를 조회하는 메서드입니다.
     * @return iptime 설정페이지에서 조회한 MAC 주소 리스트입니다.
     * @throws IOException
     */
    private List<String> getIptimeMacAddressList() throws IOException {
        List<String> list = iptime.queryMacAddressList(iptime.queryCookieValue());

        if (list.isEmpty()) {
            iptime.queryMacAddressList(iptime.queryCookieValue());
        }

        return list;
    }

    /**
     * 캐싱된 MAC 주소 리스트에 MAC 주소가 포함되어 있는지 확인하는 메서드입니다.
     * @param macAddress MAC 주소 값입니다.
     */
    private void isContain(String macAddress) {
        IptimeMacAddressLists macAddressList = getCurrentIptimeMacAddressList();

        if (!macAddressList.contain(macAddress)){
            throw new MacAddressValidateException(MacAddressValidateError.NOT_EXIST_MACADDRESS);
        }
    }

    @Override
    public List<Long> browseExistMembers(
        List<MacAddressDTO> registeredMacAddresses) {

        return registeredMacAddresses.stream()
            .filter(
                macAddressResponseDTO
                    -> getCurrentIptimeMacAddressList()
                    .contain(macAddressResponseDTO.getMacAddress())
            )
            .map(MacAddressDTO::getMemberId)
            .collect(Collectors.toList());
    }

}
