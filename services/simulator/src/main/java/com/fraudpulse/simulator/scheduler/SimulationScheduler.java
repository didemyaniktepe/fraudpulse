package com.fraudpulse.simulator.scheduler;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

import jakarta.annotation.PreDestroy;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.fraudpulse.simulator.application.SimulationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationScheduler {

    private final TaskScheduler taskScheduler;
    private final SimulationService simulationService;

    private ScheduledFuture<?> scheduledTask;

    public synchronized void start(Duration interval) {
        if (isRunning()) {
            return;
        }
        scheduledTask = taskScheduler.scheduleWithFixedDelay(this::dispatch, interval);
        log.info("Simulation started");
    }

    public synchronized void stop() {
        if (scheduledTask == null) {
            return;
        }

        scheduledTask.cancel(false);
        scheduledTask = null;

        log.info("Simulation stopped");
    }

    public synchronized void reschedule(Duration interval) {
        if (!isRunning()) {
            return;
        }

        scheduledTask.cancel(false);

        scheduledTask = taskScheduler.scheduleWithFixedDelay(
                this::dispatch,
                interval);

        log.info("Simulation rescheduled");
    }

    public synchronized boolean isRunning() {
        return scheduledTask != null
                && !scheduledTask.isCancelled()
                && !scheduledTask.isDone();
    }

    private void dispatch() {
        try {
            simulationService.sendTransaction();
        } catch (Exception e) {
            log.error("Transaction dispatch failed", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        stop();
    }
}
