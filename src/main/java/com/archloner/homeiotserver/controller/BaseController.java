package com.archloner.homeiotserver.controller;

import com.archloner.homeiotserver.entity.SensorReading;
import com.archloner.homeiotserver.service.SensorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    private final SensorService sensorService;

    public BaseController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    public ResponseEntity<Page<SensorReading>> getAllReadings(
            @PageableDefault(size = 20, sort = "timestampUtc", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<SensorReading> page = sensorService.getAllReadings(pageable);
        return ResponseEntity.ok(page);
    }

}
