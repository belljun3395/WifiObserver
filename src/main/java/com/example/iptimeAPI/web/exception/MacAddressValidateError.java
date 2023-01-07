package com.example.iptimeAPI.web.exception;

public enum MacAddressValidateError {

    NOT_REGISTER_MEMBER("601", "this member is not register member's mac address for our service"),
    NOT_EXIST_MACADDRESS("602", "can't find match mac address from iptime mac list"),
    ;

    private String message;
    private String code;

    MacAddressValidateError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}