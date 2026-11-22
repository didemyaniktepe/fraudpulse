
package com.fraudpulse.transaction.outbox.persistance;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fraudpulse.transaction.outbox.model.EventType;
import com.fraudpulse.transaction.outbox.model.OutboxStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tools.jackson.databind.JsonNode;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "outbox_events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Column(nullable = false)
    private String topic;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private JsonNode payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxStatus status = OutboxStatus.PENDING;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    private Instant nextRetryAt;
    private Instant publishedAt;

    public OutboxEvent(UUID aggregateId, EventType eventType, String topic, JsonNode payload) {
        this.aggregateId = Objects.requireNonNull(aggregateId);
        this.eventType = Objects.requireNonNull(eventType);
        this.topic = Objects.requireNonNull(topic);
        this.payload = Objects.requireNonNull(payload).deepCopy();
    }
}

