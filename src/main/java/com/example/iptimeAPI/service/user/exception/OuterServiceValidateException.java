package com.example.iptimeAPI.service.user.exception;

import lombok.Getter;

@Getter
public class OuterServiceValidateException extends RuntimeException {

    private final String code;

    public OuterServiceValidateException(OuterServiceException outerServiceException) {
        super(outerServiceException.getMessage());
        this.code = outerServiceException.getCode();
    }

}
