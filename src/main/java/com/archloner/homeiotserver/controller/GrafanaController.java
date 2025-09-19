package com.archloner.homeiotserver.controller;

import com.archloner.homeiotserver.entity.SensorReading;
import com.archloner.homeiotserver.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/grafana")
@RequiredArgsConstructor
@Slf4j
public class GrafanaController {

    private final SensorService sensorService;

    @GetMapping("/")
    public String home() {
        return "OK";
    }

    @GetMapping("/timeseries/{deviceId}")
    public List<Map<String, Object>> getTimeseries(
            @PathVariable String deviceId,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false, defaultValue = "500") int maxDataPoints
    ) {
        log.info("Requested timeseries for deviceId {}", deviceId);
        // Parse ISO8601 timestamps
        Instant fromInstant = (from != null) ? Instant.parse(from) : Instant.now().minus(Duration.ofHours(1));
        Instant toInstant = (to != null) ? Instant.parse(to) : Instant.now();

        List<SensorReading> readings = sensorService.getReadingsForDeviceId(deviceId).stream()
                .filter(r -> {
                    Instant ts = r.getTimestampUtc().atZone(ZoneId.systemDefault()).toInstant();
                    return !ts.isBefore(fromInstant) && !ts.isAfter(toInstant);
                })
                .sorted(Comparator.comparing(SensorReading::getTimestampUtc))
                .collect(Collectors.toList());

        // Optional: downsample if too many points
        int step = Math.max(1, readings.size() / maxDataPoints);

        List<SensorReading> sampled = new ArrayList<>();
        for (int i = 0; i < readings.size(); i += step) {
            sampled.add(readings.get(i));
        }

        // Build Grafana series
        List<Map<String, Object>> result = new ArrayList<>();

        result.add(buildSeries("temperature", sampled, r -> r.getTemperature()));
        result.add(buildSeries("humidity", sampled, r -> r.getHumidity()));
        result.add(buildSeries("light", sampled, r -> r.getLight()));

        return result;
    }

    private Map<String, Object> buildSeries(String name, List<SensorReading> readings, Function<SensorReading, Double> valueMapper) {
        Map<String, Object> series = new HashMap<>();
        series.put("target", name);
        series.put("datapoints", readings.stream()
                .map(r -> Arrays.asList(valueMapper.apply(r), r.getTimestampUtc().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                .collect(Collectors.toList()));
        return series;
    }

}
