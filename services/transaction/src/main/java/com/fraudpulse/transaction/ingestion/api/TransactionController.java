package com.fraudpulse.transaction.ingestion.api;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fraudpulse.transaction.ingestion.api.dto.request.CreateTransactionRequest;
import com.fraudpulse.transaction.ingestion.api.dto.response.TransactionResponse;
import com.fraudpulse.transaction.ingestion.application.TransactionService;
import com.fraudpulse.transaction.ingestion.mapper.TransactionResponseMapper;
import com.fraudpulse.transaction.ingestion.persistance.Transaction;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionResponseMapper transactionResponseMapper;

    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody CreateTransactionRequest request) {
        Transaction transaction = transactionService.create(request);
        return ResponseEntity.ok(transactionResponseMapper.toResponse(transaction));
    }

}