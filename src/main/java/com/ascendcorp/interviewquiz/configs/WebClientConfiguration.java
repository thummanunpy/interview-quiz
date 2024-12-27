
package com.ascendcorp.interviewquiz.configs;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Value("${bot.host}")
    private String botHost;

    @Value("${bot.client-id}")
    private String clientId;

    @Bean("botWebClient")
    public WebClient webClient() {
        return WebClient
            .builder()
            .baseUrl(botHost)
            .defaultHeader("accept", "application/json")
            .defaultHeader("X-IBM-Client-Id", clientId)
            .build();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
