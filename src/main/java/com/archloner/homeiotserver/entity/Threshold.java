package com.archloner.homeiotserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "thresholds")
@Data
public class Threshold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceId;
    private String metric;
    private Double minValue;
    private Double maxValue;

}
