package com.fraudpulse.transaction.ingestion.api;

import org.springframework.web.bind.annotation.RestController;

import com.fraudpulse.transaction.ingestion.api.dto.request.CreateTransactionRequest;
import com.fraudpulse.transaction.ingestion.api.dto.response.TransactionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    
    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody CreateTransactionRequest request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
}