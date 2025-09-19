package com.archloner.homeiotserver.service;

import com.archloner.homeiotserver.entity.Threshold;
import com.archloner.homeiotserver.repository.ThresholdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThresholdService {

    private final ThresholdRepository repo;

    public List<Threshold> getThresholds(String deviceId) {
        return repo.findByDeviceId(deviceId);
    }

    public Threshold saveThreshold(Threshold threshold) {
        return repo.save(threshold);
    }

    public void deleteThreshold(Long id) {
        repo.deleteById(id);
    }

    public boolean isOutOfRange(String deviceId, String metric, Double value) {
        List<Threshold> thresholds = repo.findByDeviceId(deviceId);
        return thresholds.stream()
                .filter(t -> t.getMetric().equalsIgnoreCase(metric))
                .anyMatch(t ->
                        (t.getMinValue() != null && value < t.getMinValue()) ||
                                (t.getMaxValue() != null && value > t.getMaxValue())
                );
    }

}
