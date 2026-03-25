package com.maple.project.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class NexonApiConfig {

    @Value("${nexon.api.base-url}")
    private String baseUrl;

    @Value("${nexon.api.api-key}")
    private String apiKey;

    @Bean
    public WebClient nexonWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("x-nxopen-api-key", apiKey)
                .build();
    }
}
