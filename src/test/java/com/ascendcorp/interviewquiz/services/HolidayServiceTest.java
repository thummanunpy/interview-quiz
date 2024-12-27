package com.ascendcorp.interviewquiz.services;

import com.ascendcorp.interviewquiz.client.BotClient;
import com.ascendcorp.interviewquiz.models.FinancialInstitutionsHolidaysData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HolidayServiceTest {

    @Mock
    private BotClient botClient;

    @InjectMocks
    private HolidayService holidayService;

    private List<FinancialInstitutionsHolidaysData> holidays;

    @Before
    public void setup() {
        holidays = new ArrayList<>();
        FinancialInstitutionsHolidaysData holiday = new FinancialInstitutionsHolidaysData();
        holiday.setDate("2024-12-31");
        holiday.setDateThai("31 ธันวาคม 2567");
        holiday.setHolidayWeekDayThai("วันอังคาร");
        holiday.setHolidayWeekDay("Tuesday");
        holiday.setHolidayDescription("New Year's Eve");
        holiday.setHolidayDescriptionThai("วันสิ้นปี");

        holidays.add(holiday);
    }

    @Test
    public void testGetHoliday() {
        when(botClient.getFinancialInstitutionsHoliday("2024")).thenReturn(holidays);

        List<FinancialInstitutionsHolidaysData> result = holidayService.getHoliday("2024");

        assertEquals(1, result.size());
        assertEquals("2024-12-31", result.get(0).getDate());
        assertEquals("31 ธันวาคม 2567", result.get(0).getDateThai());
        assertEquals("วันอังคาร", result.get(0).getHolidayWeekDayThai());
        assertEquals("Tuesday", result.get(0).getHolidayWeekDay());
        assertEquals("New Year's Eve", result.get(0).getHolidayDescription());
        assertEquals("วันสิ้นปี", result.get(0).getHolidayDescriptionThai());
        verify(botClient, times(1)).getFinancialInstitutionsHoliday(anyString());
    }

    @Test
    public void testGetNearestHoliday() {
        when(botClient.getFinancialInstitutionsHoliday(null)).thenReturn(holidays);

        FinancialInstitutionsHolidaysData result = holidayService.getNearestHoliday();
        assertNotNull(result);
        assertEquals("2024-12-31", result.getDate());
        assertEquals("31 ธันวาคม 2567", result.getDateThai());
        assertEquals("วันอังคาร", result.getHolidayWeekDayThai());
        assertEquals("Tuesday", result.getHolidayWeekDay());
        assertEquals("New Year's Eve", result.getHolidayDescription());
        assertEquals("วันสิ้นปี", result.getHolidayDescriptionThai());
    }

    @Test
    public void testGetNearestHolidayNoHoliday() {
        when(botClient.getFinancialInstitutionsHoliday(null)).thenReturn(new ArrayList<>());

        FinancialInstitutionsHolidaysData result = holidayService.getNearestHoliday();
        assertNull(result);
    }

    @Test
    public void testGetHolidayInRange() {
        when(botClient.getFinancialInstitutionsHoliday("2024")).thenReturn(holidays);

        LocalDate start = LocalDate.of(2024, 7, 7);
        LocalDate end = LocalDate.of(2024, 12, 31);
        List<FinancialInstitutionsHolidaysData> result = holidayService.getHolidayInRange(start, end);
        verify(botClient, times(1)).getFinancialInstitutionsHoliday(anyString());
        assertEquals("2024-12-31", result.get(0).getDate());
    }

    @Test
    public void testGetHolidayInRange_startDateEqualEndDate() {
        when(botClient.getFinancialInstitutionsHoliday("2024")).thenReturn(holidays);

        LocalDate start = LocalDate.of(2024, 12, 31);
        LocalDate end = LocalDate.of(2024, 12, 31);
        List<FinancialInstitutionsHolidaysData> result = holidayService.getHolidayInRange(start, end);
        verify(botClient, times(1)).getFinancialInstitutionsHoliday(anyString());
        assertEquals("2024-12-31", result.get(0).getDate());
    }


}