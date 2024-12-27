package com.ascendcorp.interviewquiz.controllers;

import com.ascendcorp.interviewquiz.models.FinancialInstitutionsHolidaysData;
import com.ascendcorp.interviewquiz.services.HolidayService;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/holiday")
public class HolidayController {

    @Autowired
    private final HolidayService holidayService;
    private final Logger logger = LoggerFactory.getLogger(HolidayController.class);

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public List<FinancialInstitutionsHolidaysData> getHoliday(
        @RequestParam(value = "year", required = false) String year
    ) {
        logger.info("Start getHoliday()");
        long start = System.nanoTime();

        List<FinancialInstitutionsHolidaysData> holidays = holidayService.getHoliday(year);

        logger.info(
            "End getHoliday() [{} ms]",
            (System.nanoTime() - start) / 1_000_000
        );
        return holidays;
    }

    @GetMapping("/nearest")
    public FinancialInstitutionsHolidaysData getNearestHoliday() {
        logger.info("Start getNearestHoliday()");
        long start = System.nanoTime();
        FinancialInstitutionsHolidaysData nearestHoliday = holidayService.getNearestHoliday();
        logger.info(
            "End getNearestHoliday() [{} ms]",
            (System.nanoTime() - start) / 1_000_000
        );
        return nearestHoliday;
    }

    @GetMapping("/range")
    public List<FinancialInstitutionsHolidaysData> getHolidayInRange(
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate
    ) {
        logger.info("Start getHolidayInRange()");
        long start = System.nanoTime();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startLocalDate = LocalDate.parse(startDate, formatter);
            LocalDate endLocalDate = LocalDate.parse(endDate, formatter);

            if (endLocalDate.isBefore(startLocalDate)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date must be after start date.");
            }
            List<FinancialInstitutionsHolidaysData> holidays = holidayService.getHolidayInRange(startLocalDate, endLocalDate);
            logger.info("End getHolidayInRange() [{} ms]", (System.nanoTime() - start) / 1_000_000);
            return holidays;

        } catch (DateTimeParseException ex) {
            logger.error("Invalid date format: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format. Please use yyyy-MM-dd.", ex);
        }
    }

}
