package com.bitreight.tasklist.util.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    private static final String DEFAULT_DATE_PATTERN = "dd-MM-yyyy";

    public static LocalDate dateFromString(String text) {
        if(text != null) {
            return LocalDate.parse(text, getFormatter());
        }
        return null;
    }

    public static String stringFromDate(LocalDate date) {
        if(date != null) {
            return getFormatter().format(date);
        }
        return null;
    }

    private static DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
    }
}
