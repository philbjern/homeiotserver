package com.archloner.homeiotserver;

import com.archloner.homeiotserver.service.ReportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final ReportService reportService;

    public ScheduledTasks(ReportService reportService) {
        this.reportService = reportService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void hourlyAverageDiscordReport() {
        reportService.generateAverageReport(1L);
    }

}
