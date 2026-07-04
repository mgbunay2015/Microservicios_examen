package com.restaurantetech.orders.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class HttpClientsConfig {

    @Bean
    public WebClient menuWebClient(@Value("${menu.service.url}") String menuServiceUrl) {
        return WebClient.builder()
                .baseUrl(menuServiceUrl)
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
