package com.fraudpulse.simulator.api;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fraudpulse.simulator.api.dto.request.UpdateIntervalRequest;
import com.fraudpulse.simulator.api.dto.response.SimulationStatusResponse;
import com.fraudpulse.simulator.config.SimulationSettings;
import com.fraudpulse.simulator.scheduler.SimulationScheduler;

import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

@RestController
@RequiredArgsConstructor
@RequestMapping("/simulation")
public class SimulationController {

    private final SimulationScheduler scheduler;
    private final SimulationSettings settings;

    @PostMapping("/start")
    public synchronized ResponseEntity<Void> start() {
        scheduler.start(settings.getInterval());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/stop")
    public synchronized ResponseEntity<Void> stop() {
        scheduler.stop();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/interval")
    public synchronized ResponseEntity<Void> updateInterval(
            @Valid @RequestBody UpdateIntervalRequest request) {
        settings.updateInterval(request.intervalSeconds());
        scheduler.reschedule(settings.getInterval());

        return ResponseEntity.noContent().build();
    }

    @Synchronized
    @GetMapping("/status")
    public SimulationStatusResponse status() {
        return new SimulationStatusResponse(scheduler.isRunning(), settings.getInterval().toSeconds());
    }
}
