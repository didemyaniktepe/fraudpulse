package com.fraudpulse.transaction.ingestion.api.dto.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.fraudpulse.transaction.domain.model.TransactionType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record CreateTransactionRequest(

                @NotBlank String accountExternalRef,

                String deviceRef,

                @NotNull TransactionType transactionType,

                @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal amount,

                @NotBlank String currency,

                @NotBlank String channel,

                @NotNull @PastOrPresent OffsetDateTime occurredAt

) {
}
