package com.archloner.homeiotserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SensorReadingRequest(
        @NotBlank String deviceId,
        @NotNull Double temperature,
        @NotNull Double humidity,
        @NotNull Double light
) {
}
