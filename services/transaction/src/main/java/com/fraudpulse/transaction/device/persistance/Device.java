package com.fraudpulse.transaction.device.persistance;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String deviceRef;
    @CreatedDate
    private Instant firstSeenAt;

    public Device(String deviceRef) {
        this.deviceRef = deviceRef;
    }
}