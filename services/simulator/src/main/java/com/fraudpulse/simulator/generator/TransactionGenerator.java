package com.fraudpulse.simulator.generator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.fraudpulse.simulator.client.payload.TransactionPayload;

@Component
public class TransactionGenerator {

    private static final String SOURCE = "SIM";
    private static final String CURRENCY = "EUR";

    private static final int ACCOUNT_COUNT = 5;
    private static final int DEVICE_COUNT = 8;
    private static final int DEVICE_MISSING_PERCENT = 20;
    private static final int HIGH_AMOUNT_PERCENT = 5;

    public TransactionPayload generate() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        String accountRef = "ACC-%03d".formatted(random.nextInt(1, ACCOUNT_COUNT + 1));

        String deviceRef = random.nextInt(100) < DEVICE_MISSING_PERCENT ? null
                : "DEVICE-%03d".formatted(random.nextInt(1, DEVICE_COUNT + 1));

        String transactionType = random.nextBoolean()
                ? "BANK_TRANSFER"
                : "CREDIT_CARD_PAYMENT";
        BigDecimal amount = generateAmount(random);
        String channel = random.nextBoolean() ? "MOBILE" : "WEB";

        return new TransactionPayload(
                accountRef,
                deviceRef,
                transactionType,
                amount,
                CURRENCY,
                channel,
                OffsetDateTime.now(ZoneOffset.UTC),
                SOURCE,
                UUID.randomUUID().toString()
        );
    }

    private BigDecimal generateAmount(ThreadLocalRandom random) {
        boolean highAmount = random.nextInt(100) < HIGH_AMOUNT_PERCENT;

        long cents = highAmount
                ? random.nextLong(500_001, 1_000_001)
                : random.nextLong(100, 300_001);

        return BigDecimal.valueOf(cents, 2);
    }

}