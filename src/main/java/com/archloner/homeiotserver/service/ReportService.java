package com.archloner.homeiotserver.service;

import com.archloner.homeiotserver.entity.ReportAlert;
import com.archloner.homeiotserver.entity.SensorReading;
import com.archloner.homeiotserver.repository.ReportRepository;
import com.archloner.homeiotserver.repository.SensorRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SensorRepository sensorRepository;
    private final DiscordAlertService discordAlertService;
    private final ReportRepository reportRepository;

    @Value("${app.devices.main.id}")
    private String deviceId;

    @Value("${discord.alerts.summary.enabled}")
    private boolean HOURLY_DISCORD_REPORT_ENABLED;

    public enum ReportType {
        DAY {
            @Override
            public String toString() {
                return this.name().toLowerCase();
            }
        },
        NIGHT {
            @Override
            public String toString() {
                return this.name().toLowerCase();
            }
        }
    }

    @Data
    public static class AverageValues {
        private double avgTemperature;
        private double avgHumidity;
        private double avgLight;
    }

    public Optional<AverageValues> generateAverageValues(Long hours) {
        Instant now = Instant.now();
        Instant from = now.minus(hours, ChronoUnit.HOURS);

        List<SensorReading> readings = sensorRepository.findByDeviceId(deviceId);

        List<SensorReading> filtered = readings.stream()
                .filter(r -> r.getTimestampUtc().isAfter(from) && r.getTimestampUtc().isBefore(now))
                .toList();

        if (filtered.isEmpty()) {
             return Optional.empty();
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

         AverageValues values = new AverageValues();
         values.setAvgTemperature(avgTemp);
         values.setAvgHumidity(avgHumidity);
         values.setAvgLight(avgLight);

         return Optional.of(values);
    }

    @Transactional
    public void generateDailyReport(ReportType type) {
        Optional<AverageValues> values = generateAverageValues(12L);

        String message = "No readings for the last 12 hours";
        if (values.isPresent()) {
            LocalDate date = LocalDate.now();
            message = String.format("Daily report for the %s %s:\nTemperature: %.2f°C\nHumidity: %.2f%%\nLight: %.2f%%",
                    type.toString(), date, values.get().getAvgTemperature(), values.get().getAvgHumidity(), values.get().getAvgLight() * 100.0);
        }

        Instant now = Instant.now();
        Instant from = now.minus(12, ChronoUnit.HOURS);

        ReportAlert report = new ReportAlert();
        report.setDeviceId(deviceId);
        report.setStartTime(from);
        report.setEndTime(now);
        report.setMessage(message);

        discordAlertService.sendAlert(message);

        report.setSent(true);
        reportRepository.save(report);
    }

}
