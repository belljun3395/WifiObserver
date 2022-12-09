package com.example.iptimeAPI.domain.macAddress;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MacAddresses {

    private final List<MacAddress> macAddresses;

}
