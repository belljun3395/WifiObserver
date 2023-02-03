package com.example.iptimeAPI.service.iptime;

import com.example.iptimeAPI.mapper.macAddress.MacAddressDTO;
import com.example.iptimeAPI.domain.iptime.Iptime;
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

    private List<String> getIptimeMacAddressList() throws IOException {
        List<String> list = iptime.queryMacAddressList(iptime.queryCookieValue());

        if (list.isEmpty()) {
            iptime.queryMacAddressList(iptime.queryCookieValue());
        }

        return list;
    }

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
