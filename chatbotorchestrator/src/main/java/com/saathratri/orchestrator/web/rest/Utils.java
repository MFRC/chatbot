package com.saathratri.orchestrator.web.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
   
    public static String getDateString(Long dateLong) throws ParseException  {
        Date date = new Date(dateLong);
        return DATE_FORMATTER.format(getDateWithoutTimeUsingFormat(date));
    }

    public static Date getDateWithoutTimeUsingFormat(Date date) throws ParseException {
        return DATE_FORMATTER.parse(DATE_FORMATTER.format(date));
    }

    public static String getDateInTimeZone(Long date, String timeZoneId) {
        DATE_FORMATTER.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        return DATE_FORMATTER.format(new Date(date));
    }
}