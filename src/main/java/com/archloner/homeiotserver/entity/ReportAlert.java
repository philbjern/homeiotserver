package com.archloner.homeiotserver.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "alert_reports")
@Data
public class ReportAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceId;
    private Instant startTime;
    private Instant endTime;

    @Column(columnDefinition = "TEXT")
    private String message;

    private boolean sent;
}
