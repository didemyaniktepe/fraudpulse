package com.fraudpulse.simulator.generator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.fraudpulse.simulator.client.request.TransactionPayload;

@Component
public class TransactionGenerator {

    public TransactionPayload generate() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String accountRef = "ACC-%03d".formatted(random.nextInt(1, 6));
        String deviceRef = random.nextInt(10) < 2 ? null : "DEVICE-%03d".formatted(random.nextInt(1, 9));
        String transactionType = random.nextBoolean() ? "BANK_TRANSFER" : "CREDIT_CARD_PAYMENT";
        BigDecimal amount = BigDecimal.valueOf(random.nextLong(100, 300_001), 2);
        String channel = random.nextBoolean() ? "MOBILE" : "WEB";
        String currency = "EUR";
        return new TransactionPayload(accountRef, deviceRef, transactionType,
                amount, currency, channel, OffsetDateTime.now(ZoneOffset.UTC));

    }
}