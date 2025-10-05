package com.incode.task.backend.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ThirdPartyClientConfig {

    /**
     * In production-grade setup, I would create connection pooling, and also some read and connection timeout limits.
     * I would also consider some other protocol for communication between the services such as gRPC.
     */
    @Bean
    public RestClient thirdPartyRestClient(@Value("${incode.backend.third-party-service-url}") String thirdPartyServiceUrl) {

        return RestClient.create(thirdPartyServiceUrl);
    }
}
