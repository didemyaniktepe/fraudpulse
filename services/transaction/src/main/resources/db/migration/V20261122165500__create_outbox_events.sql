CREATE TABLE outbox_events
(
    id            UUID PRIMARY KEY,
    aggregate_id  UUID         NOT NULL,
    event_type    VARCHAR(100) NOT NULL,
    topic         VARCHAR(100) NOT NULL,
    payload       JSONB        NOT NULL,

    status        VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    retry_count   INTEGER      NOT NULL DEFAULT 0,

    created_at    TIMESTAMPTZ  NOT NULL,
    next_retry_at TIMESTAMPTZ,
    published_at  TIMESTAMPTZ,

    CONSTRAINT chk_outbox_status CHECK (status IN ('PENDING', 'PUBLISHED', 'FAILED')),
    CONSTRAINT chk_outbox_retry_count CHECK (retry_count >= 0)
);

CREATE INDEX idx_outbox_pending ON outbox_events (next_retry_at, created_at) WHERE status = 'PENDING';
