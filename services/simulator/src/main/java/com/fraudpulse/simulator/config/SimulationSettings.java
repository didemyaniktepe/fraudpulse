package com.fraudpulse.simulator.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimulationSettings {

    private Duration interval;

    public SimulationSettings(@Value("${simulator.interval-seconds:60}") long seconds) {
        updateInterval(seconds);
    }

    public synchronized Duration getInterval() {
        return interval;
    }

    public synchronized void updateInterval(long seconds) {
        if (seconds < 1) {
            throw new IllegalArgumentException(
                    "Interval must be at least 1 second");
        }

        this.interval = Duration.ofSeconds(seconds);
    }
}