package com.religuard.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component

public class HealthCheckScheduler {
    @Autowired
    private HealthCheckService healthCheckService;

    @Scheduled(fixedRateString = "${scheduler.healthcheck.interval:60000}")
    public void runHealthChecks() {
        System.out.println("Running scheduled health checks...");
        healthCheckService.checkAllUrls();
    }
}
