package com.ascendcorp.interviewquiz.services;

import com.ascendcorp.interviewquiz.client.BotClient;
import com.ascendcorp.interviewquiz.models.FinancialInstitutionsHolidaysData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HolidayService {

    private final Logger logger = LoggerFactory.getLogger(HolidayService.class);
    private final BotClient botClient;

    public HolidayService(BotClient botClient) {
        this.botClient = botClient;
    }

    public List<FinancialInstitutionsHolidaysData> getHoliday(String year) {
        logger.info("entering getHoliday : {}", year);
        return botClient.getFinancialInstitutionsHoliday(year);
    }

    public FinancialInstitutionsHolidaysData getNearestHoliday() {
        logger.info("entering getNearestHoliday");

        LocalDate now = LocalDate.now();
        List<FinancialInstitutionsHolidaysData> holidays = botClient.getFinancialInstitutionsHoliday(null);
        if (holidays.isEmpty()) {
            logger.warn("No holidays found");
            return null;
        }

        FinancialInstitutionsHolidaysData nearestHoliday = holidays.stream()
                .filter(holiday -> LocalDate.parse(holiday.getDate()).isAfter(now))
                .min(Comparator.comparing(FinancialInstitutionsHolidaysData::getDate))
                .orElse(null);

        return nearestHoliday;
    }

    public List<FinancialInstitutionsHolidaysData> getHolidayInRange(LocalDate startLocalDate, LocalDate endLocalDate) {
        logger.info("entering getHolidayInRange");

        List<FinancialInstitutionsHolidaysData> allHolidays = new ArrayList<>();
        int startYear = startLocalDate.getYear();
        int endYear = endLocalDate.getYear();

        for (int year = startYear; year <= endYear; year++) {
            List<FinancialInstitutionsHolidaysData> yearlyHolidays = botClient.getFinancialInstitutionsHoliday(String.valueOf(year));
            for (FinancialInstitutionsHolidaysData holiday : yearlyHolidays) {
                LocalDate holidayDate = LocalDate.parse(holiday.getDate());
                if (holidayDate.isAfter(startLocalDate) && holidayDate.isBefore(endLocalDate) || holidayDate.isEqual(startLocalDate) || holidayDate.isEqual(endLocalDate)) {
                    allHolidays.add(holiday);
                }
            }
        }

        return allHolidays;
    }
}
