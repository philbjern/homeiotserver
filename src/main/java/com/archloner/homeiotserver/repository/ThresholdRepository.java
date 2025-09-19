package com.archloner.homeiotserver.repository;

import com.archloner.homeiotserver.entity.Threshold;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThresholdRepository extends JpaRepository<Threshold, Long> {
    List<Threshold> findByDeviceId(String deviceId);
}
