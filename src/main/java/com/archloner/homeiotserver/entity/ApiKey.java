package com.archloner.homeiotserver.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "api_keys")
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceId;

    @Column(unique = true)
    private String apiKey;

    private Instant createdAt = Instant.now();

}
