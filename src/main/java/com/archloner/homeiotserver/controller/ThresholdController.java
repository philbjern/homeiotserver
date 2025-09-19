package com.archloner.homeiotserver.controller;

import com.archloner.homeiotserver.entity.Threshold;
import com.archloner.homeiotserver.service.ThresholdService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thresholds")
@RequiredArgsConstructor
public class ThresholdController {

    private final ThresholdService thresholdService;

    @GetMapping("/{deviceId}")
    public List<Threshold> getThresholds(@PathVariable String deviceId) {
        return thresholdService.getThresholds(deviceId);
    }

    @PostMapping
    public Threshold addThreshold(@RequestBody Threshold threshold) {
        return thresholdService.saveThreshold(threshold);
    }

    @DeleteMapping("/{id}")
    public void deleteThreshold(@PathVariable Long id) {
        thresholdService.deleteThreshold(id);
    }

}
