package com.example.iptimeAPI.service.user.exception;

public enum OuterServiceException {

    IDP_EXCEPTION("701", "there are something problem at IDP"),
    ;


    private String message;

    private String code;


    OuterServiceException(String code, String message) {
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