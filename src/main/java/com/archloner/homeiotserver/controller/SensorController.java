package com.archloner.homeiotserver.controller;

import com.archloner.homeiotserver.entity.SensorReading;
import com.archloner.homeiotserver.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @PostMapping("/sensor")
    public ResponseEntity<SensorReading> postSensorReading(@RequestBody SensorReading request) {
        SensorReading reading = sensorService.saveReading(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reading);
    }

    @GetMapping("/sensor")
    public ResponseEntity<List<SensorReading>> getAllReadings() {
        List<SensorReading> readings = sensorService.getAllReadings();
        return ResponseEntity.status(HttpStatus.OK).body(readings);
    }

    @GetMapping("/sensor/{deviceId}")
    public ResponseEntity<List<SensorReading>> getReadingForDeviceId(@RequestParam String deviceId) {
        List<SensorReading> deviceReadings = sensorService.getReadingsForDeviceId(deviceId);
        return ResponseEntity.status(HttpStatus.OK).body(deviceReadings);
    }

}
