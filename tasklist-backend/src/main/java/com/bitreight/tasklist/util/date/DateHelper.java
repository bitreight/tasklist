package com.bitreight.tasklist.util.date;

import java.sql.Date;
import java.time.LocalDate;

public class DateHelper {

    public static LocalDate getMinLocalDate() {
        return LocalDate.of(1970, 1, 1);
    }

    public static LocalDate getMaxLocalDate() {
        return new Date(Long.MAX_VALUE).toLocalDate();
    }

}
