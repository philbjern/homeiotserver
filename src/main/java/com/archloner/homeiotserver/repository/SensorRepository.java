package com.archloner.homeiotserver.repository;

import com.archloner.homeiotserver.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<SensorReading, Long> {

    List<SensorReading> findByDeviceId(String deviceId);

}
