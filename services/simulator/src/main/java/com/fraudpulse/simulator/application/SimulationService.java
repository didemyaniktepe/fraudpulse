package com.fraudpulse.simulator.application;

import org.springframework.stereotype.Service;

import com.fraudpulse.simulator.client.TransactionClient;
import com.fraudpulse.simulator.client.request.TransactionPayload;
import com.fraudpulse.simulator.generator.TransactionGenerator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SimulationService {

    private final TransactionGenerator generator;
    private final TransactionClient client;

    public void sendTransaction() {
        TransactionPayload payload = generator.generate();
        client.send(payload);
    }
}
