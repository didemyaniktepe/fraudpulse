package com.fraudpulse.transaction.ingestion.application;

import com.fraudpulse.transaction.ingestion.api.dto.request.CreateTransactionRequest;
import com.fraudpulse.transaction.ingestion.api.dto.response.TransactionResponse;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TransactionService {
    
    
    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        return new TransactionResponse(UUID.randomUUID(), "PENDING", OffsetDateTime.now());
    }
}
