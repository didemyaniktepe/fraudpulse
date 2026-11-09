package com.fraudpulse.transaction.ingestion.api.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        OffsetDateTime createdAt
) {
}

