package com.fraudpulse.simulator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean("transactionRestClient")
    public RestClient transactionRestClient(
            RestClient.Builder builder,
            @Value("${simulator.transaction-api-url}") String url) {
        return builder
                .baseUrl(url)
                .build();
    }
}
