package com.archloner.homeiotserver.service;

import com.archloner.homeiotserver.entity.ReportAlert;
import com.archloner.homeiotserver.entity.SensorReading;
import com.archloner.homeiotserver.repository.ReportRepository;
import com.archloner.homeiotserver.repository.SensorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SensorRepository sensorRepository;
    private final DiscordAlertService discordAlertService;
    private final ReportRepository reportRepository;

    @Value("${app.devices.main.id}")
    private String deviceId;

    @Value("${discord.alerts.summary.enabled}")
    private final boolean HOURLY_DISCORD_REPORT_ENABLED;

    @Transactional
    public void generateAverageReport(Long hours) {
        Instant now = Instant.now();
        Instant from = now.minus(hours, ChronoUnit.HOURS);

        List<SensorReading> readings = sensorRepository.findByDeviceId(deviceId);

        List<SensorReading> filtered = readings.stream()
                .filter(r -> r.getTimestampUtc().isAfter(from) && r.getTimestampUtc().isBefore(now))
                .toList();

        if (filtered.isEmpty()) {
             discordAlertService.sendAlert("No readings in the last " + hours + " hours.");
             return;
        }

        double avgTemp = filtered.stream()
                .mapToDouble(SensorReading::getTemperature)
                .average()
                .orElse(Double.NaN);

         double avgHumidity = filtered.stream()
                .mapToDouble(SensorReading::getHumidity)
                .average()
                .orElse(Double.NaN);

         double avgLight = filtered.stream()
                .mapToDouble(SensorReading::getLight)
                .average()
                .orElse(Double.NaN);

         String message = String.format("Average sensor values for last %d hour:\nTemperature: %.2f°C\nHumidity: %.2f%%\nLight: %.2f%%",
                 hours, avgTemp, avgHumidity, avgLight * 100.0);

        ReportAlert report = new ReportAlert();
        report.setDeviceId(deviceId);
        report.setStartTime(from);
        report.setEndTime(now);
        report.setMessage(message);

        if (HOURLY_DISCORD_REPORT_ENABLED) {
            discordAlertService.sendAlert(message);
        }

        report.setSent(true);
        reportRepository.save(report);
    }

}
