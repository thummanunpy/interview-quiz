package com.ascendcorp.interviewquiz.controllers;

import com.ascendcorp.interviewquiz.models.FinancialInstitutionsHolidaysData;
import com.ascendcorp.interviewquiz.services.HolidayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class HolidayControllerTest {

    @Mock
    private HolidayService holidayService;

    private HolidayController holidayController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        holidayController = new HolidayController(holidayService);
        mockMvc = MockMvcBuilders.standaloneSetup(holidayController).build();
    }

    @Test
    public void getHoliday_returnsHolidays() throws Exception {
        FinancialInstitutionsHolidaysData holiday = new FinancialInstitutionsHolidaysData();
        holiday.setDate("2024-12-31");
        List<FinancialInstitutionsHolidaysData> holidays = Collections.singletonList(holiday);

        when(holidayService.getHoliday("2024")).thenReturn(holidays);

        mockMvc.perform(get("/holiday?year=2024"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getNearestHoliday_returnsNearestHoliday() throws Exception {
        FinancialInstitutionsHolidaysData holiday = new FinancialInstitutionsHolidaysData();
        holiday.setDate("2024-12-31");

        when(holidayService.getNearestHoliday()).thenReturn(holiday);

        mockMvc.perform(get("/holiday/nearest"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getHolidayInRange_returnsHolidaysInRange() throws Exception {
        FinancialInstitutionsHolidaysData holiday = new FinancialInstitutionsHolidaysData();
        holiday.setDate("2024-12-31");
        List<FinancialInstitutionsHolidaysData> holidays = Collections.singletonList(holiday);
        LocalDate startDate = LocalDate.parse("2024-12-31");
        LocalDate endDate = LocalDate.parse("2024-12-31");
        when(holidayService.getHolidayInRange(startDate, endDate)).thenReturn(holidays);

        mockMvc.perform(get("/holiday/range?startDate=2024-12-31&endDate=2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getHolidayInRange_startDateAfterEndDate_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/holiday/range?startDate=2024-12-31&endDate=2024-12-30"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getHolidayInRange_wrongFormatDate_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/holiday/range?startDate=2024-12-31A&endDate=2024-12-30B"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void getHolidayInRange_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/holiday/range"))
                .andExpect(status().isBadRequest());
    }


}