package com.fraudpulse.simulator.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateIntervalRequest(
        @NotNull @Min(value = 1, message = "Interval must be at least 1 second") Long intervalSeconds) {
}