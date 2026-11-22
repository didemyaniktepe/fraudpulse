package com.fraudpulse.transaction.shared.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    INVALID_RISK_SCORE("INVALID_RISK_SCORE", "Risk score must be between 0 and 100", HttpStatus.BAD_REQUEST),
    FRAUD_ASSESSMENT_TRANSACTION_MISMATCH("FRAUD_ASSESSMENT_TRANSACTION_MISMATCH","Fraud assessment does not belong to the specified transaction",HttpStatus.INTERNAL_SERVER_ERROR),
    RESOLUTION_FAILED("RESOLUTION_FAILED","Resolution has failed", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
