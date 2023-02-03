package com.example.iptimeAPI.service.macAddress.exception;

import lombok.Getter;

@Getter
public class MacAddressValidateException extends RuntimeException {

    private final String code;


    public MacAddressValidateException(MacAddressValidateError macAddressValidateError) {
        super(macAddressValidateError.getMessage());
        this.code = macAddressValidateError.getCode();
    }

}
