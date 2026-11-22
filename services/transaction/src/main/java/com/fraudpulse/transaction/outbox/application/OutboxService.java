package com.fraudpulse.transaction.outbox.application;

import org.springframework.stereotype.Service;

import com.fraudpulse.transaction.config.KafkaTopicsProperties;
import com.fraudpulse.transaction.outbox.event.FraudAssessedEvent;
import com.fraudpulse.transaction.outbox.mapper.EventMapper;
import com.fraudpulse.transaction.outbox.persistance.OutboxEvent;
import com.fraudpulse.transaction.outbox.persistance.OutboxEventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxEventRepository repository;
    private final KafkaTopicsProperties topics;
    private final EventMapper mapper;

    public void enqueue(FraudAssessedEvent event) {
        OutboxEvent outboxEvent = mapper.toOutboxEvent(event, topics.fraudEvents());
        repository.save(outboxEvent);
    }

}
