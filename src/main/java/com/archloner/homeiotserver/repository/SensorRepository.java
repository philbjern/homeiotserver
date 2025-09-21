package com.archloner.homeiotserver.repository;

import com.archloner.homeiotserver.entity.SensorReading;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface SensorRepository extends JpaRepository<SensorReading, Long> {

    List<SensorReading> findByDeviceId(String deviceId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM SensorReading r WHERE r.timestampUtc < :cutoff")
    int deleteOlderThan(Instant cutoff);

}
