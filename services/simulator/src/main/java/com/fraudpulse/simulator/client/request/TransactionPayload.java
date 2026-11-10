package com.fraudpulse.simulator.client.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionPayload(
    String accountId,
    String deviceId,
    String transactionType,
    BigDecimal amount,
    String currency,
    String channel,
    OffsetDateTime occurredAt
) {
}
