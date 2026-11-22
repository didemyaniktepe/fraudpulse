package com.fraudpulse.transaction.shared.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;

    private ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static ApiException of(ErrorCode errorCode) {
        return new ApiException(errorCode);
    }
}
