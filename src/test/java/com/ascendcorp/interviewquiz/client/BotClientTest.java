package com.ascendcorp.interviewquiz.client;

import com.ascendcorp.interviewquiz.models.BotResponse;
import com.ascendcorp.interviewquiz.models.FinancialInstitutionsHolidaysData;
import com.ascendcorp.interviewquiz.models.FinancialInstitutionsHolidaysResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BotClientTest {

    private WebClient webClient;

    private static BotClient botClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() {
        mockWebServer = new MockWebServer();
        WebClient mockedWebClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        botClient = new BotClient(mockedWebClient, "/holidays");
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.close();
    }

    @Test
    void getFinancialInstitutionsHoliday_success() throws Exception {
        FinancialInstitutionsHolidaysData holiday = new FinancialInstitutionsHolidaysData();
        holiday.setDate("2024-12-31");
        FinancialInstitutionsHolidaysResponse response = new FinancialInstitutionsHolidaysResponse();
        response.setData(Collections.singletonList(holiday));

        BotResponse botResponse = new BotResponse();
        botResponse.setResult(response);

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(botResponse))
                .addHeader("Content-Type", "application/json"));

        List<FinancialInstitutionsHolidaysData> result = botClient.getFinancialInstitutionsHoliday("2024");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("2024-12-31", result.get(0).getDate());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/holidays?year=2024", request.getPath());
    }

    @Test
    void getFinancialInstitutionsHoliday_nullResult() throws Exception {
        BotResponse botResponse = new BotResponse();
        botResponse.setResult(null);

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(botResponse))
                .addHeader("Content-Type", "application/json"));

        List<FinancialInstitutionsHolidaysData> result = botClient.getFinancialInstitutionsHoliday("2024");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/holidays?year=2024", request.getPath());
    }



    @Test
    void getFinancialInstitutionsHoliday_httpError() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.BAD_REQUEST.value())
                .setBody("Bad Request"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            botClient.getFinancialInstitutionsHoliday("2024");
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/holidays?year=2024", request.getPath());
    }

    @Test
    void getFinancialInstitutionsHoliday_unexpectedError() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setBody("Internal Server Error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            botClient.getFinancialInstitutionsHoliday("2024");
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/holidays?year=2024", request.getPath());
    }

}