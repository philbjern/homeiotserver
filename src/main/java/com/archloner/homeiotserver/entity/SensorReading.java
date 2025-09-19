package com.archloner.homeiotserver.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name = "readings")
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceId;

    @Column(name = "timestamp_utc", nullable = false)
    private Instant timestampUtc;

    @Column(name = "epoch_ms", nullable = false)
    private Long epochMs;

    private Double temperature;

    private Double humidity;

    private Double light;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.timestampUtc = now;
        this.epochMs = now.toEpochMilli();
    }

}
