package com.archloner.homeiotserver.service;

import com.archloner.homeiotserver.config.DiscordProperties;
import com.archloner.homeiotserver.entity.SensorReading;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlertDispatcher {

    private final ThresholdService thresholdService;
    private final DiscordService discordService;
    private final DiscordProperties discordProperties;

    @Async
    public void maybeSendThresholdAlerts(SensorReading reading) {
        if (!discordProperties.isEnabled()) {
            return;
        }

        Map<String, Double> metrics = Map.of(
                "temperature", reading.getTemperature(),
                "humidity", reading.getHumidity(),
                "light", reading.getLight()
        );

        metrics.entrySet().stream()
                .filter(entry -> thresholdService.isOutOfRange(
                        reading.getDeviceId(),
                        entry.getKey(),
                        entry.getValue()
                ))
                .forEach(entry -> discordService.sendMessage(
                        String.format("%s out of range: %s", capitalize(entry.getKey()), entry.getValue())
                ));
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

}
