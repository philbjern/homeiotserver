package com.archloner.homeiotserver.controller;

import com.archloner.homeiotserver.entity.SensorReading;
import com.archloner.homeiotserver.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/grafana")
@RequiredArgsConstructor
public class GrafanaController {

    private final SensorService sensorService;

    @GetMapping("/timeseries/{deviceId}")
    public List<Map<String, Object>> getTimeseries(@PathVariable String deviceId) {
        List<SensorReading> readings = sensorService.getReadingsForDeviceId(deviceId);

        List<Map<String, Object>> result = new ArrayList<>();

        // Temperature
        Map<String, Object> tempSeries = new HashMap<>();
        tempSeries.put("target", "temperature");
        tempSeries.put("datapoints", readings.stream()
                .map(r -> Arrays.asList(r.getTemperature(), r.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .collect(Collectors.toList()));
        result.add(tempSeries);

        // Humidity
        Map<String, Object> humSeries = new HashMap<>();
        humSeries.put("target", "humidity");
        humSeries.put("datapoints", readings.stream()
                .map(r -> Arrays.asList(r.getHumidity(), r.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .collect(Collectors.toList()));
        result.add(humSeries);

        // Light
        Map<String, Object> lightSeries = new HashMap<>();
        lightSeries.put("target", "light");
        lightSeries.put("datapoints", readings.stream()
                .map(r -> Arrays.asList(r.getLight(), r.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .collect(Collectors.toList()));
        result.add(lightSeries);

        return result;
    }

}
