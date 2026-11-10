package com.fraudpulse.simulator.api.dto.response;

public record SimulationStatusResponse(
        boolean running,
        long intervalSeconds) {
}
