package com.fraudpulse.transaction.shared.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransactionExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ProblemDetail> handleApiException(ApiException exception) {
        ErrorCode code = exception.getErrorCode();

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(code.getStatus(), code.getMessage());
        problem.setTitle(code.name());
        problem.setProperty("errorCode", code.getCode());

        return ResponseEntity.status(code.getStatus()).body(problem);
    }

}
