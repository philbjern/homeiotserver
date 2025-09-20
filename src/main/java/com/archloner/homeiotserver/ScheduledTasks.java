package com.archloner.homeiotserver;

import com.archloner.homeiotserver.repository.SensorRepository;
import com.archloner.homeiotserver.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@Slf4j
public class ScheduledTasks {

    private final ReportService reportService;
    private final SensorRepository sensorRepository;

    @Value("${cleanup.retention-days}")
    private int retentionDays;

    public ScheduledTasks(ReportService reportService, SensorRepository sensorRepository) {
        this.reportService = reportService;
        this.sensorRepository = sensorRepository;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void hourlyAverageDiscordReport() {
        reportService.generateAverageReport(1L);
    }

    // Run every day at 3 AM
    @Scheduled(cron = "0 0 3 * * *")
    public void deleteOldReadings() {
        Instant cutoff = Instant.now().minus(retentionDays, ChronoUnit.DAYS);
        int deleted = sensorRepository.deleteOlderThan(cutoff);
        log.info("Deleted {} old sensor readings older than {} days.", deleted, retentionDays);
    }

}
