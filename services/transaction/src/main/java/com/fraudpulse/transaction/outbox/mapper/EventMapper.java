package com.fraudpulse.transaction.outbox.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.fraudpulse.transaction.config.MapStructConfig;
import com.fraudpulse.transaction.fraud.model.FraudResult;
import com.fraudpulse.transaction.ingestion.persistance.Transaction;
import com.fraudpulse.transaction.outbox.event.FraudAssessedEvent;
import com.fraudpulse.transaction.outbox.persistance.OutboxEvent;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, config = MapStructConfig.class)
public interface EventMapper {

    @Mapping(target = "transactionId", source = "transaction.id")
    @Mapping(target = "riskScore", source = "result.riskScore")
    @Mapping(target = "riskLevel", source = "result.riskLevel")
    @Mapping(target = "transactionStatus", source = "transaction.status")
    FraudAssessedEvent toEvent(Transaction transaction, FraudResult result);

    @Mapping(target = "aggregateId", source = "event.transactionId")
    @Mapping(target = "eventType", constant = "FRAUD_ASSESSED")
    @Mapping(target = "topic", source = "topic")
    @Mapping(target = "payload", source = "event", qualifiedByName = "toJson")
    OutboxEvent toOutboxEvent(FraudAssessedEvent event, String topic);

}
