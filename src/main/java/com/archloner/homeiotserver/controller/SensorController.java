package com.archloner.homeiotserver.controller;

import com.archloner.homeiotserver.dto.SensorReadingRequest;
import com.archloner.homeiotserver.entity.SensorReading;
import com.archloner.homeiotserver.service.AlertDispatcher;
import com.archloner.homeiotserver.service.SensorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensor")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;
    private final AlertDispatcher alertDispatcher;

    @PostMapping
    public ResponseEntity<SensorReading> saveReading(@Valid @RequestBody SensorReadingRequest request) {
        SensorReading reading = sensorService.saveReading(request);

        alertDispatcher.maybeSendThresholdAlerts(reading);
        return ResponseEntity.created(null).body(reading);
    }

    @GetMapping
    public ResponseEntity<List<SensorReading>> getAllReadings() {
        List<SensorReading> readings = sensorService.getAllReadings();
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<List<SensorReading>> getReadingForDeviceId(@PathVariable String deviceId) {
        List<SensorReading> deviceReadings = sensorService.getReadingsForDeviceId(deviceId);
        return ResponseEntity.ok(deviceReadings);
    }

}
