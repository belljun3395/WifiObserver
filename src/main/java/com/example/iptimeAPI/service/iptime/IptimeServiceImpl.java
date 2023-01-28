package com.example.iptimeAPI.service.iptime;

import com.example.iptimeAPI.domain.iptime.Iptime;
import com.example.iptimeAPI.domain.iptime.IptimeMacAddressLists;
import com.example.iptimeAPI.domain.iptime.IptimeMacAddressListsRepository;
import com.example.iptimeAPI.domain.iptime.IptimeService;
import com.example.iptimeAPI.domain.macAddress.MacAddress;
import com.example.iptimeAPI.service.iptime.dto.IpResponseDTO;
import com.example.iptimeAPI.service.macAddress.exception.MacAddressValidateError;
import com.example.iptimeAPI.service.macAddress.exception.MacAddressValidateException;
import com.example.iptimeAPI.web.dto.IpDTO;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class IptimeServiceImpl implements IptimeService {

    private final Iptime iptime;

    private final IptimeMacAddressListsRepository repository;

    private final String serviceIp;

    private String cookieValue;



    @Autowired
    public IptimeServiceImpl(Iptime iptime, IptimeMacAddressListsRepository repository) throws IOException {
        this.iptime = iptime;
        this.serviceIp = iptime.connectIpInfo();
        this.cookieValue = iptime.getCookieValue();
        this.repository = repository;
    }

    @Override
    public IpResponseDTO isInIptime(IpDTO ipDTO) {
        return new IpResponseDTO(iptime.isIn(ipDTO.getIp()));
    }

    @Override
    public void isExistMacAddress(String macAddress) throws IOException {
        try {
            isContain(macAddress);
        } catch (MacAddressValidateException macAddressValidateException) {
            this.renewalList();
            this.isContain(macAddress);
        }
    }

    //    @Scheduled(fixedDelay = 3000)
    @Scheduled(fixedDelay = 60000 * 60)
    public void renewalList() throws IOException {
        List<String> latestMacAddressesList = this.getIptimeMacAddressList();

        IptimeMacAddressLists currentIptimeMacAddressList = getCurrentIptimeMacAddressList();

        if (!currentIptimeMacAddressList.isSameMacAddressList(latestMacAddressesList)) {
            IptimeMacAddressLists iptimeMacAddressLists =
                new IptimeMacAddressLists(
                    serviceIp,
                    latestMacAddressesList
                );

            repository.deleteAll();

            repository.save(iptimeMacAddressLists);
        }
    }

    private IptimeMacAddressLists getCurrentIptimeMacAddressList() {
        return repository.findByIp(serviceIp)
            .orElseGet(() -> {
                try {
                    return new IptimeMacAddressLists(serviceIp, this.getIptimeMacAddressList());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    private List<String> getIptimeMacAddressList() throws IOException {
        List<String> list = iptime.getList(cookieValue);

        if (list.isEmpty()) {
            this.cookieValue = iptime.getCookieValue();
            this.iptime.getList(cookieValue);
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
        List<MacAddress.MacAddressResponseDTO> registeredMacAddresses) {

        return registeredMacAddresses.stream()
            .filter(
                macAddressResponseDTO
                    -> getCurrentIptimeMacAddressList()
                    .contain(macAddressResponseDTO.getMacAddress())
            )
            .map(MacAddress.MacAddressResponseDTO::getMemberId)
            .collect(Collectors.toList());
    }
}
