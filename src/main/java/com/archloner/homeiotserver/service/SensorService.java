package com.archloner.homeiotserver.service;

import com.archloner.homeiotserver.entity.SensorReading;
import com.archloner.homeiotserver.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorService {

    private final SensorRepository sensorRepository;

    public SensorReading saveReading(SensorReading request) {
        SensorReading reading = sensorRepository.save(request);
        log.info("Received a sensor reading: {}", reading);
        return reading;
    }

    public List<SensorReading> getAllReadings() {
        return sensorRepository.findAll();
    }

    public List<SensorReading> getReadingsForDeviceId(String deviceId) {
        return sensorRepository.findByDeviceId(deviceId);
    }

}
