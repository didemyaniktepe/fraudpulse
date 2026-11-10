package com.fraudpulse.simulator.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fraudpulse.simulator.client.payload.TransactionPayload;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionClient {

    private final RestClient restClient;

    public void send(TransactionPayload payload) {
        restClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toBodilessEntity();
    }

}