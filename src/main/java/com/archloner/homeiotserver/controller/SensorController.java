package com.archloner.homeiotserver.controller;

import com.archloner.homeiotserver.entity.SensorReading;
import com.archloner.homeiotserver.service.DiscordAlertService;
import com.archloner.homeiotserver.service.SensorService;
import com.archloner.homeiotserver.service.ThresholdService;
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

    private final ThresholdService thresholdService;

    private final DiscordAlertService discordAlertService;

    private final boolean discordAlertsEnabled = false;

    @PostMapping("/sensor")
    public ResponseEntity<SensorReading> saveReading(@RequestBody SensorReading request) {
        SensorReading reading = sensorService.saveReading(request);

        if (discordAlertsEnabled) {
            // check thresholds
            if (thresholdService.isOutOfRange(reading.getDeviceId(), "temperature", reading.getTemperature())) {
                discordAlertService.sendAlert("Temperature out of range: " + reading.getTemperature() + "°C");
            }

            if (thresholdService.isOutOfRange(reading.getDeviceId(), "humidity", reading.getHumidity())) {
                discordAlertService.sendAlert("Humidity out of range: " + reading.getHumidity() + "%");
            }

            if (thresholdService.isOutOfRange(reading.getDeviceId(), "light", reading.getLight())) {
                discordAlertService.sendAlert("Light out of range: " + reading.getLight() * 100 + "%");
            }
        }
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
