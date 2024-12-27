package com.ascendcorp.interviewquiz.client;

import com.ascendcorp.interviewquiz.models.BotResponse;
import com.ascendcorp.interviewquiz.models.FinancialInstitutionsHolidaysData;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

@Component
public class BotClient {

    private final Logger logger = LoggerFactory.getLogger(BotClient.class);
    @Qualifier("botWebClient")
    private final WebClient webClient;
    private final String financialInstitutionsHolidaysUri;

    public BotClient(WebClient webClient, @Value("${bot.financial-institutions-holidays-uri}") String financialInstitutionsHolidaysUri) {
        this.webClient = webClient;
        this.financialInstitutionsHolidaysUri = financialInstitutionsHolidaysUri;
    }

    @Cacheable(value = "holidays", key = "#year != null ? #year : 'default'")
    public List<FinancialInstitutionsHolidaysData> getFinancialInstitutionsHoliday(String year) {
        logger.info("call getFinancialInstitutionsHoliday with year: {}", year);

        try {
            BotResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(financialInstitutionsHolidaysUri)
                            .queryParam("year", year) // Use query parameter
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(BotResponse.class)
                    .block();

            return Objects.requireNonNull(response).getResult() != null ? response.getResult().getData() : Collections.emptyList();

        } catch (WebClientResponseException e) {
            logger.error("HTTP error while calling bot service: Status Code {}, Response Body {}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new ResponseStatusException(e.getStatusCode(), e.getResponseBodyAsString(), e);
        }
    }
}
